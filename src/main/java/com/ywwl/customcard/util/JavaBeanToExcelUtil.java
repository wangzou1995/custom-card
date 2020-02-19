package com.ywwl.customcard.util;

import com.ywwl.customcard.model.ProductPriceModel;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;

/**
 * JavaBean转换Excel
 */
public class JavaBeanToExcelUtil {
    private static final String [] TITLES = {
           "产品编号","产品名称","服务类型","生效日期","重量段"
    };
    public static XSSFWorkbook invoke (List<ProductPriceModel> data, XSSFWorkbook workbook) {
        // 获取excel对象 初始化样式
        initBook(workbook);
        // 写标题
        writeTitle(workbook);
        return null;
    }

    /**
     * 写标题
     * @param workbook
     */
    private static void writeTitle(XSSFWorkbook workbook) {
    }

    /**
     * 初始化数据
     * @param workbook
     */
    private static void initBook(XSSFWorkbook workbook) {

    }
}
