/*
package com.progressive.minds.chimera.core.datahub.emitter;

import com.linkedin.metadata.query.filter.*;
import com.linkedin.metadata.query.*;
import com.linkedin.metadata.search.SearchResult;
import datahub.client.Emitter;
import datahub.client.rest.RestEmitter;

import java.util.Collections;

import static com.progressive.minds.chimera.core.datahub.common.genericUtils.DATAHUB_AUTH_TOKEN;
import static com.progressive.minds.chimera.core.datahub.common.genericUtils.DATAHUB_URL;

public class SearchByUrnExample {
    public static void main(String[] args) throws Exception {
        // Initialize EntityClient (assuming you have Restli client and authentication setup)

        // Define the URN to search for
        String urnToSearch = "urn:li:dataJob:(sample-flow,sample-job)";
        CriterionArray criterionArray = new CriterionArray();
        Criterion criterion = new Criterion().setField("urn") // Field to filter by
                .setValue(urnToSearch) // The URN value
                .setCondition(Condition.EQUAL);
        criterionArray.add(criterion);

        // Build the filter using the URN
        Filter filter = new Filter()
                .setCriteria(criterionArray);


        // Perform the search using the filter
        SearchResult searchResult = search(
                "dataJob",   // Entity type (e.g., dataJob, dataset, etc.)
                "",          // Empty query string (we're only filtering)
                filter,      // The filter built above
                null,        // Sort criteria (null means default sorting)
                0,           // Starting from record 0
                10,          // Return up to 10 results
                */
/* Authentication *//*
 null // Add your authentication token here
        );

        // Print search results
        searchResult.getEntities().forEach(entity -> {
            System.out.println("Found entity URN: " + entity.getUrn());
        });
    }
}
*/
