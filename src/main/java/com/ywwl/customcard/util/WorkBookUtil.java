package com.ywwl.customcard.util;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class WorkBookUtil {
    /**
     * 合并单元格
     *
     * @param sheet  sheet
     * @param region region
     */
    public static void myAddMerged(XSSFSheet sheet, CellRangeAddress region) {
        // 下边框
        RegionUtil.setBorderBottom(BorderStyle.THIN, region, sheet);
        // 左边框
        RegionUtil.setBorderLeft(BorderStyle.THIN, region, sheet);
        // 右边框
        RegionUtil.setBorderRight(BorderStyle.THIN, region, sheet);
        // 上边框
        RegionUtil.setBorderTop(BorderStyle.THIN, region, sheet);
        sheet.addMergedRegion(region);
    }

}
