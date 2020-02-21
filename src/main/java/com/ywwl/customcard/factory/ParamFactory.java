package com.ywwl.customcard.factory;

import com.ywwl.customcard.model.Param;
import com.ywwl.customcard.model.ParamTypeEnum;
import com.ywwl.customcard.model.QueryLinePriceParam;
import com.ywwl.customcard.model.QueryPriceParam;
import com.ywwl.customcard.util.CountryApplicationRunner;
import com.ywwl.customcard.util.ProductApplicationRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ParamFactory {
    @Autowired
    CountryApplicationRunner countryApplicationRunner;
    @Autowired
    ProductApplicationRunner productApplicationRunner;
    public  Param getParam(ParamTypeEnum paramType){

        switch (paramType){
            case ROUTE:
                QueryLinePriceParam queryLinePriceParam = new QueryLinePriceParam();
                queryLinePriceParam.setProductCodes(productApplicationRunner.getProductCodes());
                queryLinePriceParam.setCountryCodes(countryApplicationRunner.getCountryModels());
                return queryLinePriceParam;
            case PRICE:
                QueryPriceParam queryPriceParam = new QueryPriceParam();
                queryPriceParam.setCountryModels(countryApplicationRunner.getCountryModels());
                return queryPriceParam;
            default:
                return null;
        }
    }
}
