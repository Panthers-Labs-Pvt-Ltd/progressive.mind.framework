import com.linkedin.common.urn.Urn;
import com.linkedin.common.urn.UrnUtils;
import com.linkedin.metadata.aspect.patch.builder.StructuredPropertiesPatchBuilder;
import com.linkedin.mxe.MetadataChangeProposal;
import datahub.client.MetadataWriteResponse;
import datahub.client.rest.RestEmitter;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class DatasetStructuredPropertiesExample {

    public static void main(String[] args) {
        // Example dataset URN (this should be your actual dataset URN)
        String datasetUrn = "urn:li:dataset:(urn:li:dataPlatform:postgres,obdef.public.data_sources_connections,PROD)";

   /*     // Define custom structured properties
        Map<String, Object> customProperties1 = new HashMap<>();
        customProperties.put("owner", "John Doe");
        customProperties.put("sensitivity", "high");
        customProperties.put("dataClassification", "confidential");
*/

        List<String> customPropertiesList = new ArrayList<>();

        // Add custom properties as strings to the list
        customPropertiesList.add("owner: John Doe");
        customPropertiesList.add("sensitivity: high");
        customPropertiesList.add("dataClassification: confidential");



        // Create the StructuredProperties object with the custom properties
        MetadataChangeProposal structuredProperties = null;
        try {
            structuredProperties = new StructuredPropertiesPatchBuilder()
                    .urn(UrnUtils.getUrn(datasetUrn))
                    .setStringProperty(Urn.createFromString(datasetUrn), customPropertiesList).build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        String token = "";
        RestEmitter emitter = RestEmitter.create(b -> b.server("http://localhost:8080").token(token));
        Future<MetadataWriteResponse> response1 = null;
        try {
            response1 = emitter.emit(structuredProperties, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            System.out.println(response1.get().getResponseContent());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
