package com.progressive.minds.chimera.core.datahub.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.linkedin.common.urn.GlossaryNodeUrn;
import com.linkedin.common.urn.Urn;
import com.linkedin.common.url.Url;
import com.linkedin.data.ByteString;
import com.linkedin.data.template.SetMode;
import com.linkedin.data.template.StringMap;
import com.linkedin.glossary.GlossaryTermInfo;
import com.linkedin.mxe.GenericAspect;
import com.linkedin.mxe.MetadataChangeProposal;
import datahub.client.MetadataWriteResponse;
import datahub.client.rest.RestEmitter;
import com.linkedin.common.url.UrlCoercer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Future;

import static com.linkedin.data.template.SetMode.REMOVE_IF_NULL;
import static com.linkedin.data.template.SetMode.REMOVE_OPTIONAL_IF_NULL;
import static com.linkedin.events.metadata.ChangeType.UPSERT;


public class CreateGlossaryTerm {
    public static void main(String[] args) {
        try {
            // Set Source URL
            Url sc = new Url("http://example.com/glossary-term-details");
            GlossaryNodeUrn parentNodeUrn = GlossaryNodeUrn.createFromString("urn:li:glossaryNode:XXXXX");


            GlossaryTermInfo termInfo = new GlossaryTermInfo()
                    .setName("CUSTOM_dataProductUrn")
                    .setDefinition("MMAMXMD DSD SDD sdsd SDSD")
                    .setTermSource("INTERNAL")
                    .setSourceRef("INTERNAL-")
                    .setId("manishKumarGuptaId")
                    .setRawSchema("DEPRECATED")
                   .setParentNode(GlossaryNodeUrn.createFromUrn(parentNodeUrn), REMOVE_OPTIONAL_IF_NULL)
                    //.setParentNode(GlossaryNodeUrn.createFromString(aa.toString()))
                    .setSourceUrl(sc, REMOVE_OPTIONAL_IF_NULL);


            // Set Custom Properties
            StringMap customProperties = new StringMap();
            customProperties.put("NAME", "Manish");
            termInfo.setCustomProperties(customProperties);

           // String in= termInfo.toString().replace("urn:li:glossaryNode:XXXXX", "{}");

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(termInfo); // Serialize DataMap to JSON
            ByteString byteStringTermInfo = ByteString.copyAvroString(jsonString, true); // Convert JSON string to ByteString
            GlossaryTermInfo deserializedTermInfo = objectMapper.readValue(byteStringTermInfo.asInputStream(), GlossaryTermInfo.class);
            System.out.println("deserializedTermInfo JSON: " + deserializedTermInfo);

            // Set Parent Node (Optional)
            // GlossaryNodeUrn parentNodeUrn = GlossaryNodeUrn.createFromString("urn:li:glossaryNode:9c46e657-552d-4b30-9de7-ec87aba95410");
            // termInfo.setParentNode(parentNodeUrn.getIdAsInt()); // Uncomment if required

            // Serialize GlossaryTermInfo
          //
            System.out.println("Request JSON: " + byteStringTermInfo);
/*
            objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
           String jsonStringTermInfo = objectMapper.writeValueAsString(in);
            ByteString byteStringTermInfo = ByteString.unsafeWrap(jsonStringTermInfo.getBytes(StandardCharsets.UTF_8));*/


            // Print Request JSON

            // Create GenericAspect
            GenericAspect genericAspectTermInfo = new GenericAspect();
            genericAspectTermInfo.setValue(byteStringTermInfo);
            genericAspectTermInfo.setContentType("application/json");

            if (genericAspectTermInfo.getValue() == null) {
                throw new IllegalArgumentException("Aspect value cannot be null");
            }

            // Initialize Emitter
            String DATAHUB_URL = "http://localhost:8080";
            String DATAHUB_AUTH_TOKEN = "";
            RestEmitter emitter = RestEmitter.create(b -> b
                    .server(DATAHUB_URL)
                    .token(DATAHUB_AUTH_TOKEN));

            // Create MetadataChangeProposal
            Urn glossaryTermKeyUrn = Urn.createFromString("urn:li:glossaryTerm:exampleTerm123");
            MetadataChangeProposal proposalTermInfo = new MetadataChangeProposal();
            proposalTermInfo.setEntityUrn(glossaryTermKeyUrn);
            proposalTermInfo.setEntityType("glossaryTerm");
            proposalTermInfo.setAspectName("glossaryTermInfo");
            proposalTermInfo.setAspect(genericAspectTermInfo);
            proposalTermInfo.setChangeType(UPSERT);

            // Emit Proposal
            Future<MetadataWriteResponse> responseTermInfo = emitter.emit(proposalTermInfo, null);
            String returnCodeTermInfo = responseTermInfo.get().getResponseContent();

            // Handle Response
            if (returnCodeTermInfo.contains("success")) {
                System.out.println("Glossary term created successfully! " + returnCodeTermInfo);
            } else {
                System.out.println("Failed to create glossary term. Response: " + returnCodeTermInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to create the glossary term.");
        }
    }
}
