package com.ywwl.customcard.util;
import com.ywwl.customcard.model.AbstractPriceModel;
import org.apache.poi.ss.usermodel.Workbook;

import	java.util.List;

import java.util.Map;

/**
 *   Object to workbook
 */
public class Object2WorkbookUtil {


    public static <T extends AbstractPriceModel> Workbook getWorkbook(Map<String,String> kv, List<T> priceEntities) {
        Object obj = (T) priceEntities.get(0);
        return null;
    }
}
