package com.ywwl.customcard.factory;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import java.util.HashMap;
import java.util.Map;

public class WorkBookFactory {
    private static final long TITLE_SIZE = 12L;
    private static final long CONTEXT_SIZE = 10L;


    public static Map<XSSFWorkbook,Map<String, Short>> getInstance() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Map<String, Short> styleMap = initStyle(workbook);
        Map<XSSFWorkbook, Map<String, Short> > result = new HashMap<XSSFWorkbook, Map<String, Short> > ();
        result.put(workbook, styleMap);
        return result;
    }

    private static Map<String, Short> initStyle(XSSFWorkbook workbook) {
        Map<String, Short> styleMap = new HashMap<>();
        styleMap.put("title", initTitleStyle(workbook));
        styleMap.put("context", initContextStyle(workbook));
        return styleMap;

    }

    /**
     * 初始化内容样式
     *
     * @param workbook book对象
     * @return 样式Index
     */
    private static Short initContextStyle(XSSFWorkbook workbook) {
        XSSFCellStyle contextStyle = workbook.createCellStyle();
        // 居中显示
        contextStyle.setAlignment(HorizontalAlignment.CENTER);
        // 垂直居中
        contextStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        setBorderStyle(contextStyle);
        XSSFFont font = workbook.createFont();
        font.setFontName("微软雅黑");
        font.setFontHeight(CONTEXT_SIZE);
        font.setBold(false);
        contextStyle.setFont(font);
        return contextStyle.getIndex();
    }

    /**
     * 初始化标题样式
     *
     * @param workbook book对象
     * @return 样式Index
     */
    private static short initTitleStyle(XSSFWorkbook workbook) {
        XSSFCellStyle titleStyle = workbook.createCellStyle();
        // 居中显示
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        // 垂直居中
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        setBorderStyle(titleStyle);
        XSSFFont font = workbook.createFont();
        font.setFontName("微软雅黑");
        font.setFontHeight(TITLE_SIZE);
        font.setBold(true);
        titleStyle.setFont(font);
        return titleStyle.getIndex();
    }

    /**
     * 设置单元格边框样式
     *
     * @param style 样式对象
     */
    private static void setBorderStyle(XSSFCellStyle style) {
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
    }
}
