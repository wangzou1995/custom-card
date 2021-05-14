package com.ywwl.customcard.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ywwl.customcard.factory.WorkBookFactory;
import com.ywwl.customcard.model.CountryModel;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * JavaBean转换Excel
 */
@Component
public class JavaBeanToExcelUtil {
    private static final String[] TITLES = {
            "产品编号", "产品名称", "服务类型", "生效日期", "重量段"
    };
    private static final String PRICE_STR = "价格(元/KG)";
    private static final String FEE_STR = "处理费(元/件)";
    private static final String SHEET_NAME = "报价单";
    @Autowired
    private CountryApplicationRunner countryApplicationRunner;


    public List<Map<String, XSSFWorkbook>> invoke(List<JSONObject> data) {
        return data.parallelStream().map(this::createWorkbook).collect(Collectors.toList());
    }

    private XSSFWorkbook getWorkbook(JSONObject jsonObject) {
        Map<XSSFWorkbook, Map<String, Short>> map = WorkBookFactory.getInstance();
        XSSFWorkbook workbook = null;
        Map<String, Short> styleMap = null;
        for (XSSFWorkbook wk : map.keySet()) {
            workbook = wk;
            styleMap = map.get(wk);
        }
        assert workbook != null;
        XSSFSheet sheet = workbook.createSheet(SHEET_NAME);
        CellStyle cellStyle = workbook.getCellStyleAt(styleMap.get("title"));
        writeTitle(sheet, cellStyle);

        Map<String, CellStyle> contextStyles = new HashMap<>();
        contextStyles.put("context", workbook.getCellStyleAt(styleMap.get("context")));
        writeContext(sheet, contextStyles, jsonObject);
        sheet.createFreezePane(5, 2, 5, 2);
        return workbook;
    }

    private void writeContext(XSSFSheet sheet, Map<String, CellStyle> styleMap, JSONObject jsonObject) {
        List<CountryModel> countryModels = countryApplicationRunner.getCountryModels();
        JSONArray productsData = jsonObject.getJSONArray("data");
        for (Object json : productsData) {
            JSONObject jo = (JSONObject) json;
            JSONArray productData = jo.getJSONArray("data");
            List<String> productMessages = getProductMessages(jo);
            if(productData == null){
                return;
            }
            int weightNum = productData.size();
            int rowNum = sheet.getLastRowNum() + 1;
            XSSFRow row = sheet.createRow(rowNum);
            CellStyle cellStyle = styleMap.get("context");
            for (String str : productMessages) {
                int cellNum = row.getLastCellNum();
                int currentNum = cellNum == -1 ? 0 : cellNum;
                XSSFCell cell = row.createCell(currentNum);
                cell.setCellValue(str);
                cell.setCellStyle(cellStyle);
                if (weightNum > 1) {
                    CellRangeAddress cra = new CellRangeAddress(rowNum, rowNum + weightNum - 1, currentNum, currentNum);
                    WorkBookUtil.myAddMerged(sheet, cra);
                }
            }

            for (int i = 0; i < weightNum; i++) {
                JSONObject data = productData.getJSONObject(i);
//                XSSFRow dataRow = sheet.createRow(sheet.getLastRowNum());
//                sheet.createRow(sheet.getLastRowNum()+1);
                XSSFRow dataRow = sheet.getRow(rowNum + i);
                XSSFCell cell = dataRow.createCell(dataRow.getLastCellNum());
                cell.setCellValue(getWeightStr(data));
                cell.setCellStyle(cellStyle);
                // 写数据
                JSONArray array = data.getJSONArray("data");
                for (CountryModel countryModel : countryModels) {
                    Double[] prices = getPrice(array, countryModel.getCountryCode());
                    if (prices == null) {
                        for (int j = 0; j < 2; j++) {
                            XSSFCell cell1 = dataRow.createCell(dataRow.getLastCellNum());
                            cell1.setCellValue("--");
                            cell1.setCellStyle(cellStyle);
                        }
                    } else {
                        for (Double price : prices) {
                            XSSFCell cell1 = dataRow.createCell(dataRow.getLastCellNum());
                            cell1.setCellValue(price);
                            cell1.setCellStyle(cellStyle);
                        }
                    }
                }
            }
            // 写数据

        }


    }

    private Double[] getPrice(JSONArray data, String countryCode) {
        Double[] doubles = new Double[2];
        JSONObject priceObj = findPriceByCountryCode(data, countryCode);
        if (priceObj == null) {
            return null;
        } else {
            doubles[0] = priceObj.getDouble("price");
            doubles[1] = priceObj.getDouble("fee");
            return doubles;
        }
    }

    private JSONObject findPriceByCountryCode(JSONArray data, String countryCode) {
        List<JSONObject> list = data.toJavaList(JSONObject.class);
        Optional<JSONObject> aa1 =
                list.parallelStream().filter(e -> e.getString("countryCode").equals(countryCode)).findFirst();
        JSONObject jo = null;
        if (aa1.isPresent()) {
            jo = aa1.get();
        }
        return jo;

    }

    private static String getWeightStr(JSONObject data) {
        Double weightFrom = data.getDouble("weightFrom");
        Double weightTo = data.getDouble("weightTo");
        return weightFrom + "-" + weightTo + "KG";
    }

    private static List<String> getProductMessages(JSONObject jsonObject) {
        List<String> products = new ArrayList<>(4);
        JSONObject product = jsonObject.getJSONObject("message");
        String effectTime = jsonObject.getString("effectTime");
        products.add(product.getString("productCode"));
        products.add(product.getString("productName"));
        products.add(product.getString("serviceType"));
        products.add(effectTime);
        return products;
    }

    private void writeTitle(XSSFSheet sheet, CellStyle cellStyle) {
        List<CountryModel> countryModels = countryApplicationRunner.getCountryModels();

        XSSFRow row1 = sheet.createRow(0);
        XSSFRow row2 = sheet.createRow(1);
        for (String title : TITLES) {
            int cellNum = row1.getLastCellNum();
            int currentNum = cellNum == -1 ? 0 : cellNum;
            XSSFCell cell = row1.createCell(currentNum);
            cell.setCellValue(title);
            cell.setCellStyle(cellStyle);
            CellRangeAddress cra = new CellRangeAddress(0, 1, currentNum, currentNum);
            WorkBookUtil.myAddMerged(sheet, cra);
        }
        for (CountryModel countryModel : countryModels) {
            String countryName = countryModel.getCountryName();
            int cellNum = row1.getLastCellNum();
            XSSFCell cell1 = row1.createCell(cellNum);
            cell1.setCellValue(countryName);
            cell1.setCellStyle(cellStyle);
            CellRangeAddress cra = new CellRangeAddress(0, 0, cellNum, cellNum + 1);
            WorkBookUtil.myAddMerged(sheet, cra);
            XSSFCell cell2 = row2.createCell(cellNum);
            cell2.setCellValue(PRICE_STR);
            cell2.setCellStyle(cellStyle);
            XSSFCell cell3 = row2.createCell(cellNum + 1);
            cell3.setCellValue(FEE_STR);
            cell3.setCellStyle(cellStyle);
        }
        // 设置列宽
        for (int i = 0; i < TITLES.length + countryModels.size() * 2; i++) {
            sheet.autoSizeColumn(i, true);
            sheet.setColumnWidth(i, i == 1 ? 7000 : 5000);
        }
    }

    private Map<String, XSSFWorkbook> createWorkbook(JSONObject e) {
        Map<String, XSSFWorkbook> map = new HashMap<>();
        map.put(e.getString("companyCode"), getWorkbook(e));
        return map;
    }
}
