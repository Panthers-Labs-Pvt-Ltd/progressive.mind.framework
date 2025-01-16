package com.progressive.minds.chimera.core.datahub.common;
import com.linkedin.common.urn.Urn;
import com.linkedin.data.ByteString;
import com.linkedin.data.template.RecordTemplate;
import com.linkedin.events.metadata.ChangeType;
import com.linkedin.mxe.GenericAspect;
import com.linkedin.mxe.MetadataChangeProposal;
import datahub.client.Emitter;
import datahub.client.MetadataWriteResponse;
import datahub.client.rest.RestEmitter;
import datahub.client.Callback;
import datahub.shaded.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class genericUtils {
static String DATAHUB_URL = "http://localhost:8080";
static String DATAHUB_AUTH_TOKEN = "";

    // Method to create MetadataChangeProposal for DataProduct
    public static MetadataChangeProposal createProposal(String entityUrn, String entityType,
                                                                   String aspectName, String changeType,
                                                                   RecordTemplate aspect)
    {
        try {
           GenericAspect genericAspect = serializeAspect(aspect);

            // Create MetadataChangeProposal
            MetadataChangeProposal proposal = new MetadataChangeProposal()
                    .setEntityUrn(Urn.createFromString(entityUrn)) // Set// the Data Product URN
                    .setEntityType(entityType) // Set the entity type to "dataProduct"
                    .setAspectName(aspectName) // Aspect name
                    .setAspect(genericAspect) // Set the GenericAspect
                    .setChangeType(ChangeType.valueOf(changeType)); // Change type: UPSERT

            return proposal;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create MetadataChangeProposal for Data Product", e);
        }
    }

    public static GenericAspect serializeAspect(RecordTemplate aspect) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(aspect.data());
        ByteString byteString = ByteString.copyAvroString(jsonString, true);  // Serialize to ByteString

        GenericAspect genericAspect = new GenericAspect();
        genericAspect.setValue(byteString);  // Set the serialized aspect
        genericAspect.setContentType("application/json");  // Content type
        return genericAspect;
    }
    public static String replaceSpecialCharsAndLowercase(String input) {
        if (input == null) {
            return null;
        }
        // Replace all non-alphanumeric characters with empty string
        String cleanedString = input.replaceAll("[^a-zA-Z0-9]", "");

        // Convert to lowercase and return
        return cleanedString.toLowerCase();
    }

    public static String emitProposalRest(MetadataChangeProposal mcpw, String Type) throws IOException {
        RestEmitter emitter;
        final String[] status = new String[1];
        final String responseEntityUrn = mcpw.getEntityUrn().toString();
                emitter = RestEmitter.create(b -> b
                .server(DATAHUB_URL) // Replace with your DataHub server URL
                .token(DATAHUB_AUTH_TOKEN)        // Replace with your token if required
        );

        Future<MetadataWriteResponse> response = emitter.emit(mcpw, new Callback() {
            @Override
            public void onCompletion(MetadataWriteResponse response) {
                if (response.isSuccess()) {
                    status[0] = mcpw.getEntityUrn().getId();
                    System.out.println(String.format(Type + "Successfully emitted metadata event for %s", mcpw.getEntityUrn()));
                } else {
                     status[0] = response.getResponseContent();
                    System.out.println(String.format(Type + "Failed to emit metadata event for %s, aspect: %s with status code: %d",
                            mcpw.getEntityUrn(), mcpw.getAspectName(), response.getResponseContent()));
                }
            }

            @Override
            public void onFailure(Throwable exception) {
                status[0] = exception.getMessage();
                System.out.println(String.format(Type + "Failed to emit metadata event for %s, aspect: %s due to %s",
                        mcpw.getEntityUrn(), mcpw.getAspectName(), exception.getMessage()));
            }
        });
        return status[0];
    }

    public static String emitProposal(MetadataChangeProposal proposal, String Type) throws IOException, ExecutionException, InterruptedException {
        Emitter emitter = RestEmitter.create(b -> b
                .server(DATAHUB_URL) // Replace with your DataHub server URL
                .token(DATAHUB_AUTH_TOKEN)        // Replace with your token if required
        );

        Future<MetadataWriteResponse> response = emitter.emit(proposal, null);
        String returnCode = response.get().getResponseContent();
        String returnValue;
        if (returnCode.contains("success"))
        {
            returnValue=  proposal.getEntityUrn().toString();
        System.out.println(Type + " created successfully!");
        }
        else
        {
            System.out.println(returnCode);
            returnValue = returnCode;
        }
        return returnValue;
    }

}
