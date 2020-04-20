package com.ywwl.customcard.controller;
import java.util.ArrayList;
import	java.util.List;
import	java.util.HashMap;

import com.ywwl.customcard.model.AbstractPriceModel;
import com.ywwl.customcard.model.KGAndProcessFocusModel;
import com.ywwl.customcard.util.Object2WorkbookUtil;

import java.util.Map;

public class DownloadCardTestController {
    public static void main(String[] args) {
        KGAndProcessFocusModel kgAndProcessFocusModel = new KGAndProcessFocusModel();
        Map<String,String> kv = new HashMap<String, String> ();
        List <KGAndProcessFocusModel> list = new ArrayList<>();
        list.add(kgAndProcessFocusModel);
        Object2WorkbookUtil.getWorkbook(kv,list);
    }

}
