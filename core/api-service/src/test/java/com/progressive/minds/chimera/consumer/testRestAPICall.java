package com.progressive.minds.chimera.consumer;

import org.junit.jupiter.api.Test;
import static com.progressive.minds.chimera.consumer.restAPICall.*;

import java.io.IOException;

public class testRestAPICall {
    @Test
    public void testGetToken() throws IOException, InterruptedException{
        String token  = getToken();
        System.out.println("token : " + token);
    }

    @Test
    public void testApiCall() throws IOException, InterruptedException {
        String response = consumeAPI("http://localhost:8080/api/v1/extractMetadataConfig");
        System.out.println("extractConfig : " + response);

    }

   
}
