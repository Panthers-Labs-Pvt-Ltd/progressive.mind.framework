/*
package com.progressive.minds.chimera.core.datahub;

import com.linkedin.metadata.query.filter.*;
import com.linkedin.metadata.search.SearchResult;
import datahub.client.rest.RestEmitter;
import com.linkedin.data.template.StringArray;
import com.linkedin.metadata.query.AnyResult;
import io.datahubproject.openapi.generated.SearchEntity;
import io.datahubproject.openapi.generated.SearchEntity.SearchEntityBuilder;
import io.datahubproject.openapi.generated.SearchEntity;


import com.linkedin.r2.transport.common.ClientFactory;
import com.linkedin.r2.transport.http.client.HttpClientFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.linkedin.metadata.query.filter.Filter;
import com.linkedin.metadata.query.AnyResult;


import java.util.Optional;

import static com.linkedin.metadata.query.filter.Condition.EQUAL;

public class DataHubSearchExample {

    public static void main(String[] args) {
        String dataHubServer = "http://localhost:8080"; // Replace with your DataHub server URL
        String authToken = "your-auth-token"; // Optional: Provide your token if authentication is enabled

        // Initialize RestEmitter
        RestEmitter emitter = RestEmitter.createWithDefaults();
        Criterion  C = new Criterion();
        C.setField("urn");
        C.setValue("urn:li:dataProduct:chimeradataproduct4");
        C.setCondition(EQUAL);
        C.hasValue();

        Criterion anotherCriterion = new Criterion();
        anotherCriterion.setField("aspect");
        anotherCriterion.setValue("domains");
        anotherCriterion.setCondition(EQUAL);

        CriterionArray CA = new CriterionArray();
        CA.add(C);
        CA.add(anotherCriterion);

        Filter filter = new Filter();
        filter.setCriteria(CA);
    System.out.println(filter);

        // Create an HTTP client
        Map<String, Object> clientOptions = new HashMap<>();
        clientOptions.put(HttpClientFactory.HTTP_REQUEST_TIMEOUT, 30000);
        ClientFactory clientFactory = new HttpClientFactory();


        // Perform a search query
        /// SearchResult searchResult = searchForDataProduct(emitter, "myDataProduct");

        // Extract and print search results
*/
/*        searchResult.getEntities().forEach(entity -> {
            System.out.println("Entity Type: " + entity.getEntity().getEntityType());
            System.out.println("URN: " + entity.getEntity().getIdAsUrn());
        });*//*


    }

}
*/
