package com.progressive.minds.chimera.core.datahub.referances;

public interface DataHubUtils {
    String SYSTEM_USER = "System";


  /*  static String searchEntity(String input) {

        String dataProductId = "urn:li:dataProduct:<your-data-product-id>";

        // Initialize DataHubClient (Make sure to set up your DataHub client properly)
        DataHubClient client = new DataHubClient("<your-datahub-instance-url>");

        try {
            // Search for the data product using its ID
            EntitySearchRequest searchRequest = new EntitySearchRequest();
            searchRequest.setQuery(dataProductId); // Search by the data product ID
            searchRequest.setType("dataProduct");  // Search for data product type

            EntitySearchResult result = client.search(searchRequest);

            // Process and print the result
            if (result.getEntities().size() > 0) {
                Entity dataProduct = result.getEntities().get(0); // Get the first match
                System.out.println("Found Data Product: " + dataProduct.getUrn());
            } else {
                System.out.println("No data product found with the provided ID.");
            }
        } catch (ApiException e) {
            e.printStackTrace();
            System.out.println("Error while searching for data product.");
        }
    }*/
    }
