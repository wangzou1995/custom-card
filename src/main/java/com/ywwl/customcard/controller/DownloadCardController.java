package com.ywwl.customcard.controller;

import com.alibaba.fastjson.JSONObject;
import com.ywwl.customcard.model.LinePriceModel;
import com.ywwl.customcard.model.RequestDownloadModel;
import com.ywwl.customcard.model.RequestWISHModel;
import com.ywwl.customcard.model.RetResult;
import com.ywwl.customcard.service.CountryWeightPriceService;
import com.ywwl.customcard.service.DSService;
import com.ywwl.customcard.service.WishCardService;
import com.ywwl.customcard.util.FileUtil;
import com.ywwl.customcard.util.JavaBeanToExcelUtil;
import com.ywwl.customcard.util.RetResponse;
import com.ywwl.customcard.util.WishJavaBeanToExcelUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class DownloadCardController {
    private static final Logger logger = LoggerFactory.getLogger(DownloadCardController.class);
    @Autowired
    private CountryWeightPriceService countryWeightPriceService;
    @Autowired
    private WishCardService wishCardService;
    @Autowired
    private DSService dsService;
    @Autowired
    private JavaBeanToExcelUtil javaBeanToExcelUtil;

    @PostMapping(value = "/getLine")
    public RetResult<List<LinePriceModel>> getLine(@Valid @RequestBody RequestDownloadModel requestDownloadModel) {
        return RetResponse.makeOKRsp(countryWeightPriceService.getLine(requestDownloadModel));
    }

    @PostMapping(value = "/getPrice")
    public RetResult<List<JSONObject>> test(@Valid @RequestBody RequestDownloadModel requestDownloadModel) {
        return RetResponse.makeOKRsp(countryWeightPriceService.getMessageByEffectTime(requestDownloadModel));
    }
    @CrossOrigin
    @PostMapping(value = "/download")
    public void download(@Valid @RequestBody RequestDownloadModel requestDownloadModel, HttpServletResponse response) {
        List<Map<String, XSSFWorkbook>> workbooks = javaBeanToExcelUtil.invoke(countryWeightPriceService.getMessageByEffectTime(requestDownloadModel));
        String path = FileUtil.getFilePath();
        workbooks.parallelStream().forEach(e -> {
                    try {
                        for (String key : e.keySet()) {
                            XSSFWorkbook workbook = e.get(key);
                            workbook.write(new FileOutputStream(new File(path + File.separator + countryWeightPriceService.getCompanyNameByCode(key))));
                            workbook.close();
                        }
                    } catch (IOException ex) {
                        logger.error("写出文件异常" + ex);
                    }

                });
        FileUtil.downloadFile(path, response);
    }
    @PostMapping(value = "/getWishPrice")
    public RetResult<List<List < Map<String, Object>>>> getWishPrice(@Valid @RequestBody RequestWISHModel requestWISHModel) {

        List<List<Map<String, Object>>> result = getWishPriceList(requestWISHModel);
        return RetResponse.makeOKRsp(result);
    }

    private  List<List<Map<String, Object>>>  getWishPriceList(@RequestBody @Valid RequestWISHModel requestWISHModel) {
        List<List<Map<String, Object>>> result = new ArrayList<>();
        List<String> productCodes =  requestWISHModel.getProductCodes();
        for (String productCode: productCodes) {
            List<Map<String, Object>> list = wishCardService.getWishCardPrice(productCode,requestWISHModel.getEffectTime());
            if (list != null && list.size() > 0) {
                result.add(list);
            }

        }
        return result;
    }
    private  List<List<Map<String, Object>>>  getDSPriceList(@RequestBody @Valid RequestWISHModel requestWISHModel) {
        List<List<Map<String, Object>>> result = new ArrayList<>();
        List<String> productCodes =  requestWISHModel.getProductCodes();
        for (String productCode: productCodes) {
            List<Map<String, Object>> list = dsService.getDSCardPrice(productCode,requestWISHModel.getEffectTime());
            if (list != null && list.size() > 0) {
                result.add(list);
            }

        }
        return result;
    }

    @CrossOrigin
    @PostMapping(value = "/wishCard/download")
    public void wishDownload(@Valid @RequestBody RequestWISHModel requestWISHModel, HttpServletResponse response) {
        List<List<Map<String, Object>>> result = getWishPriceList(requestWISHModel);
        String path = FileUtil.getFilePath();
        int i = 0;
        if (result.size() > 0) {
            for (List<Map<String, Object>> paths : result) {
                try {
                    String productCode = requestWISHModel.getProductCodes().get(i);
                    String fileName = wishCardService.getFileName(productCode);
                    String priceType = wishCardService.getPriceType(productCode, requestWISHModel.getEffectTime());
                    if (priceType.equals("price09")) {
                        priceType = wishCardService.getPriceType09(productCode, requestWISHModel.getEffectTime());
                    }
                    SXSSFWorkbook workbook = WishJavaBeanToExcelUtil.getWorkBook(paths, priceType);
                    workbook.write(new FileOutputStream(new File(path + File.separator + fileName)));
                    workbook.close();
                    i++;

                } catch (IOException ex) {
                    logger.error("写出文件异常" + ex);
                    ex.printStackTrace();
                }

            }
            FileUtil.downloadFile(path, response);
        } else {
            logger.error("价格配置异常");
        }

    }
    @CrossOrigin
    @PostMapping(value = "/wishCard/price")
    public RetResult<List<List<Map<String, Object>>>> wishPrice(@Valid @RequestBody RequestWISHModel requestWISHModel, HttpServletResponse response) {
        List<List<Map<String, Object>>> result = getWishPriceList(requestWISHModel);

        return RetResponse.makeOKRsp(result);
    }
    @CrossOrigin
    @PostMapping(value = "/dsCard/price")
    public RetResult<List<List<Map<String, Object>>>> DSPrice(@Valid @RequestBody RequestWISHModel requestWISHModel, HttpServletResponse response) {
        List<List<Map<String, Object>>> result = getDSPriceList(requestWISHModel);

        return RetResponse.makeOKRsp(result);
    }
    @CrossOrigin
    @PostMapping(value = "/dsCard/download")
    public void dsDownload(@Valid @RequestBody RequestWISHModel requestWISHModel, HttpServletResponse response) {
        List<List<Map<String, Object>>> result = getDSPriceList(requestWISHModel);
        String path = FileUtil.getFilePath();
        int i = 0;
        if (result.size() > 0) {
            for (List<Map<String, Object>> paths : result) {
                try {
                    String productCode = requestWISHModel.getProductCodes().get(i);
                    String fileName = dsService.getFileName(productCode);
                    String priceType = wishCardService.getPriceType(productCode, requestWISHModel.getEffectTime());
                    if (priceType.equals("price09")) {
                        priceType = wishCardService.getPriceType09(productCode, requestWISHModel.getEffectTime());
                    }
                    SXSSFWorkbook workbook = WishJavaBeanToExcelUtil.getDSWorkBook(paths, priceType);
                    workbook.write(new FileOutputStream(new File(path + File.separator + fileName)));
                    workbook.close();
                    i++;

                } catch (IOException ex) {
                    logger.error("写出文件异常" + ex);
                    ex.printStackTrace();
                }

            }
            FileUtil.downloadFile(path, response);
        } else {
            logger.error("价格配置异常");
        }
    }
    @CrossOrigin
    @GetMapping(value = "/dsCard/allProduct")
    public RetResult<List<Map<String, Object>>> allProduct(){
        List<Map<String,Object>> map = dsService.getAllProduct();
        return RetResponse.makeOKRsp(map);
    }
}
