package com.progressive.minds.chimera.core.datahub.dataproduct;

import datahub.shaded.org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;


class ManageDataProductTest {
@Test
void createDataProductTest()   {
    Map<String, String> map = new HashMap<>();

    // Add key-value pairs to the map
    map.put("Prop1", "Value1");
    map.put("Prop2", "Value2");
    map.put("Prop3", "Value3");

    ManageDataProduct MDP = new ManageDataProduct();
    String retval = MDP.createDataProduct("d0481baa-2896-4456-851d-2d437421c8c4","Manish Data Product 11",
            "sdfddfffffffffffffffffff", map);
    System.out.println("Product Created With URN " + retval);
}

    @Test
    void createDataProductTestWithAll()   {
        String  dataProductName = "Chimera Data Product";
        String  dataProductDescription = "My First Data Product Creation Test With All Optional Information's";
        String  domainName = "My First Data Product Domain Test";
        String externalURL = "http://datahub.com";
        String[]globalTags = {"Confidential", "Verified","PII"};
        String[]glossaryTerms = {"Balance", "Current Balance","Total Amount in Account"};

        Map<String, Pair<String, String>> DataAssets = new HashMap<>();
        DataAssets.put("baz1", Pair.of("chart", "looker"));
        DataAssets.put("baz2", Pair.of("dataset", "hive"));
        DataAssets.put("baz3", Pair.of("dashboard", "powerbi"));

        Map<String, String> Owners = new HashMap<>();
        Owners.put("manish.kumar.gupta@outlook.com", "Data Creator");
        Owners.put("Abhinav Kumar", "Data owner");

        Map<String, String> customProperties = new HashMap<>();
        Owners.put("Property1", "Value1");
        Owners.put("Property2", "Value2");

        DataProducts dataProducts = new DataProducts();
       String retVal = dataProducts.createDataProduct(dataProductName, dataProductDescription, externalURL, domainName,
                globalTags,glossaryTerms,DataAssets,Owners,customProperties );
       System.out.println("Data Product Created With URN " + retVal);
    }
}