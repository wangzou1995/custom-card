package com.ywwl.customcard.controller;

import com.alibaba.fastjson.JSONObject;
import com.ywwl.customcard.model.LinePriceModel;
import com.ywwl.customcard.model.RequestDownloadModel;
import com.ywwl.customcard.service.CountryWeightPriceService;
import com.ywwl.customcard.util.FileUtil;
import com.ywwl.customcard.util.JavaBeanToExcelUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.util.List;
import java.util.Map;

@RestController
public class DownloadCardController {
    private static final Logger logger = LoggerFactory.getLogger(DownloadCardController.class);
    @Autowired
    private CountryWeightPriceService countryWeightPriceService;
    @Autowired
    private JavaBeanToExcelUtil javaBeanToExcelUtil;

    @PostMapping(value = "/line")
    public List<LinePriceModel> getLine(@Valid @RequestBody RequestDownloadModel requestDownloadModel) {
        return countryWeightPriceService.getLine(requestDownloadModel);
    }

    @PostMapping(value = "/test")
    public List<JSONObject> test(@Valid @RequestBody RequestDownloadModel requestDownloadModel) {
        return countryWeightPriceService.getMessageByEffectTime(requestDownloadModel);
    }

    @PostMapping(value = "/download")
    public void download(@Valid @RequestBody RequestDownloadModel requestDownloadModel, HttpServletResponse response) {
        List<Map<String, XSSFWorkbook>> workbooks = javaBeanToExcelUtil.invoke(test(requestDownloadModel));
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
}
