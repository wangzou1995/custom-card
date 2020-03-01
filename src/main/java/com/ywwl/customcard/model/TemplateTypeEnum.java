package com.ywwl.customcard.model;

import lombok.Data;

/**
 * 报价单配置类型
 */

public enum TemplateTypeEnum {
    TEMPLATE01 ("price01"),TEMPLATE09("price09");
    private String value;
    TemplateTypeEnum(String type) {
        this.value = type;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    public static TemplateTypeEnum getByValue(String s) {
           TemplateTypeEnum [] templateTypeEnums = TemplateTypeEnum.values();
           for( TemplateTypeEnum templateTypeEnum : templateTypeEnums) {
               if (templateTypeEnum.getValue().equals(s)) {
                   return templateTypeEnum;
               }
           }

           return null;
    }
}
