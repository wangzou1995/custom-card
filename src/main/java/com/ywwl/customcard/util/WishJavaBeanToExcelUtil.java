package com.ywwl.customcard.util;
import java.math.BigDecimal;
import java.util.HashMap;
import	java.util.Map;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;


import java.util.List;
import java.util.Map;

public class WishJavaBeanToExcelUtil {
    /*
     * 模板一excel标题
     */
    private static String[] PT01T = { "condition", "Province", "City", "County", "Country", "Min Weight (kg)",
            "Max Weight (kg)", "Start Weight (kg)", "Logistic Fee (/kg)", "Operation Fee", "Channel","linePrice", };
    private static String[] PT01T_KEY = {"province","city","county","country","minWeight","maxWeight","startWeight",
            "logisticFee","operationFee","channel","U_Price"
    };
    /*
     * 模板二excel标题
     */
    private static String[] PT02T = { "condition", "Province", "City", "County", "Country", "Min Weight (kg)",
            "Max Weight (kg)", "Start Weight (kg)", "Start Fee", "Weight Unit (kg)", "Weight Unit Fee", "Operation Fee",
            "Channel" ,"linePrice"};
    private static String[] PT02T_KEY = {"province","city","county","country","minWeight","maxWeight","startWeight",
            "startFee","weightUnit","weightUnitFee","operationFee","channel","U_Price"
    };
    /*
     *
     *模版15excel标题
     */
    private static String[] PT15T = { "condition", "Province", "City", "County", "Country", "Min Weight (kg)",
            "Max Weight (kg)", "Start Weight (kg)", "Logistic Fee (>30g)", "Fixed Fee (<30g)","Operation Fee",
            "Channel" ,"linePrice"};

    private static String[] PT15T_KEY = {"province","city","county","country","minWeight","maxWeight","startWeight",
            "logisticFee","fixedFee","operationFee","channel","U_Price"
    };


    public static SXSSFWorkbook getWorkBook (List<Map<String, Object>> price, String priceType) {
        Map<Integer, CellStyle> styleMap = new HashMap<>();
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        initCellStyle(workbook,styleMap);
        switch (priceType) {
            case "price01":
            case "price08":
                getWorkBook(styleMap,price,workbook,PT01T,PT01T_KEY);
                break;
            case "price02":
                getWorkBook(styleMap,price,workbook,PT02T,PT02T_KEY);
                break;
            case "price15":
                getWorkBook(styleMap,price,workbook,PT15T,PT15T_KEY);
                break;
        }
        return workbook;
    }

    public static void  getWorkBook(Map<Integer, CellStyle> styleMap,List<Map<String, Object>> price, SXSSFWorkbook workbook,String [] titles,String [] keys) {
        SXSSFSheet sheet = workbook.createSheet();
        // 画标题
        SXSSFRow rowTitle = sheet.createRow(sheet.getLastRowNum()== -1 ? 0 : sheet.getLastRowNum()+1);
        for (String title: titles) {
            SXSSFCell cell = rowTitle.createCell(rowTitle.getLastCellNum()== -1 ?0 : rowTitle.getLastCellNum());
            cell.setCellValue(title);
            cell.setCellStyle(styleMap.get(0));
        }
        // 数据
        price.forEach( e-> {
            SXSSFRow rowData = sheet.createRow(sheet.getLastRowNum()== -1 ? 0 : sheet.getLastRowNum()+1);
            SXSSFCell cell = rowData.createCell(0);
            cell.setCellValue("TRUE");
            cell.setCellStyle(styleMap.get(1));
            for(int i = 0; i <keys.length ; i++) {
                String key = keys[i];
                SXSSFCell cell1 = rowData.createCell(i+1);
                if (e.containsKey(key)) {
                    Object value = e.get(key);
                    if (value == null) {
                        cell1.setBlank();
                        cell1.setCellType(CellType.BLANK);
                    } else {
                        if (value instanceof Double || value instanceof BigDecimal) {
                            cell1.setCellValue(Double.parseDouble(value.toString()));
                            cell1.setCellStyle(styleMap.get(2));
                        } else {
                            cell1.setCellValue(value.toString());
                            cell1.setCellStyle(styleMap.get(1));
                        }
                    }
                } else {
                    cell1.setBlank();
                    cell1.setCellType(CellType.BLANK);
                }
            }
        });
    }

    /**
     * 初始化excel 样式
     *
     * @param workbook
     */
    public static void initCellStyle(SXSSFWorkbook workbook, Map <Integer,CellStyle> styleMap) {

        CellStyle xcsT = workbook.createCellStyle();
        CellStyle xcsC01 = workbook.createCellStyle();
        CellStyle xcsC02 = workbook.createCellStyle();
        Font xft = workbook.createFont();

        DataFormat xff = workbook.createDataFormat();

        // 设置字体颜色
        xft.setColor(IndexedColors.BLACK.index);
        // 设置字体样式
        xft.setFontName("宋体");
        // 设置字体大小
        xft.setFontHeightInPoints((short) 11);
        // 设置字体粗细
        xft.setBold(true);

        xcsT.setFont(xft);
        // 字体居中
        xcsT.setAlignment(HorizontalAlignment.CENTER);

        Font xfc01 = workbook.createFont();
        // 设置字体颜色
        xfc01.setColor(IndexedColors.BLACK.index);
        // 设置字体样式
        xfc01.setFontName("宋体");
        xfc01.setFontHeightInPoints((short) 11);
        // 设置字体粗细
        xfc01.setBold(false);
        xcsC01.setFont(xfc01);
        xcsC01.setDataFormat(xff.getFormat("0.00"));
        xcsC01.setAlignment(HorizontalAlignment.LEFT);

        Font xfc02 = workbook.createFont();
        // 设置字体颜色
        xfc02.setColor(IndexedColors.BLACK.index);
        // 设置字体样式
        xfc02.setFontName("宋体");
        xfc02.setFontHeightInPoints((short) 11);
        // 设置字体粗细
        xfc02.setBold(false);
        xcsC02.setFont(xfc02);
        xcsC02.setDataFormat(xff.getFormat("0.000"));
        xcsC02.setAlignment(HorizontalAlignment.RIGHT);
        styleMap.put(0, xcsT);
        styleMap.put(1, xcsC01);
        styleMap.put(2, xcsC02);
    }
}
