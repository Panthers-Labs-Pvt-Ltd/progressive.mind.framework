package com.progressive.minds.chimera.core.datahub.dataproduct;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;


class ManageDataProductTest {
@Test
void createDataProductTest() throws JsonProcessingException {
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
}