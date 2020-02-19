package com.ywwl.customcard.factory;

import com.ywwl.customcard.model.Param;
import com.ywwl.customcard.model.QueryLinePriceParam;
import com.ywwl.customcard.model.QueryPriceParam;
import com.ywwl.customcard.util.StaticSource;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ParamFactory {
    @Autowired
    StaticSource staticSource;
    public  Param getParam(String paramType){

        switch (paramType){
            case "route":
                return new QueryLinePriceParam();
            case "price":
                QueryPriceParam queryPriceParam = new QueryPriceParam();
                queryPriceParam.setCountryModels(staticSource.getCountryModels());
                return queryPriceParam;
            default:
                return null;
        }
    }
}
