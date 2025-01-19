package com.progressive.minds.chimera.core.datahub.dataproduct;

import datahub.shaded.org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;


class ManageDataProductTest {
    @Test
    void createDataProductTestWithAll()   {
        String  dataProductName = "Chimera Test DP 5";
        String  dataProductDescription = "My First Data Product Creation Test With All Optional Information's";
        String  domainName = "Testing";
        String externalURL = "http://datahub.com";
        String[]globalTags = {"No Confidential", "Unverified","Test"};
        String[]glossaryTerms = {"Balance", "Current Balance","Total Amount in Account"};

        Map<String, Pair<String, String>> DataAssets = new HashMap<>();
        DataAssets.put("baz1", Pair.of("chart", "looker"));
        DataAssets.put("baz2", Pair.of("dataset", "hive"));
        DataAssets.put("baz3", Pair.of("dashboard", "powerbi"));

        Map<String, String> Owners = new HashMap<>();
        Owners.put("manish.kumar.gupta@outlook.com", "Data Creator");
        Owners.put("Prashant Kumar", "Data owner");
        Owners.put("Abhinav Kumar", "Data owner");

        Map<String, String> customProperties = new HashMap<>();
        customProperties.put("Property1", "Value1");
        customProperties.put("Property2", "Value2");

        ManageDataProduct dataProducts = new ManageDataProduct();
        String retVal = dataProducts.createDataProduct(dataProductName, dataProductDescription, externalURL, domainName,
                globalTags,glossaryTerms,DataAssets,Owners,customProperties );

     /*  String retVal = dataProducts.createDataProduct(dataProductName, dataProductDescription, externalURL, domainName,
                globalTags,glossaryTerms,DataAssets,Owners,customProperties );*/
       System.out.println("Data Product Created With URN " + retVal);
    }
}