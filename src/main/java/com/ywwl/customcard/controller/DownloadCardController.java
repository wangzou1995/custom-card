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
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

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
        List<XSSFWorkbook> workbooks = javaBeanToExcelUtil.invoke(test(requestDownloadModel));
        String path = FileUtil.getFilePath();
        AtomicInteger i = new AtomicInteger(0);
        workbooks.parallelStream().forEach(e -> {
                    try {
                        e.write(new FileOutputStream(new File(path + File.separator + UUID.randomUUID().toString() +".xlsx")));
                        i.getAndIncrement();
                        e.close();
                    } catch (IOException ex) {
                        logger.error("写出文件异常" + ex);
                    }

                }
        );
        if (FileUtil.zipFiles(FileUtil.getFiles(path), new File(path + File.separator + "download.zip"))) {
            try {
                // path是指欲下载的文件的路径。
                File file = new File(path + File.separator + "download.zip");
                // 取得文件名。
                String filename = file.getName();
                // 取得文件的后缀名。
                String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();

                // 以流的形式下载文件。
                InputStream fis = new BufferedInputStream(new FileInputStream(path + File.separator + "download.zip"));
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                fis.close();
                // 清空response
                // 设置response的Header

                response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
                response.addHeader("Content-Length", "" + file.length());

                OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
                response.setContentType("application/octet-stream");
                toClient.write(buffer);
                toClient.flush();
                toClient.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
