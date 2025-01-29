/*
package com.progressive.minds.chimera.core.datahub.common;
import com.linkedin.glossary.GlossaryTermInfo;
import com.linkedin.common.urn.Urn;
import com.linkedin.data.ByteString;
import com.linkedin.data.template.DataTemplate;
import com.linkedin.data.template.StringMap;
import com.linkedin.metadata.key.GlossaryTermKey;
import com.linkedin.mxe.GenericAspect;
import com.linkedin.mxe.MetadataChangeProposal;
import datahub.client.Emitter;
import datahub.client.MetadataWriteResponse;
import datahub.client.rest.RestEmitter;
import datahub.shaded.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static com.linkedin.events.metadata.ChangeType.CREATE;
import static com.linkedin.events.metadata.ChangeType.UPSERT;
import static com.linkedin.metadata.Constants.GLOSSARY_TERMS_ASPECT_NAME;
import static com.linkedin.metadata.Constants.GLOSSARY_TERM_ENTITY_NAME;


class ManageGlossaryTermsGroupsTest {

    @Test
    void createGlossaryTerm() throws Exception {
        ManageGlossaryTermsGroups.GlossaryRelatedTermsRecord relatedTerms = new ManageGlossaryTermsGroups.GlossaryRelatedTermsRecord(
                "Related Term Name",
                "Related Term Definition",
                null, null, null, null
        );

        // Create GlossaryTermsRecord with the related terms record
        ManageGlossaryTermsGroups.GlossaryTermsRecord glossaryTerms = new ManageGlossaryTermsGroups.GlossaryTermsRecord(
                "Glossary Term",
                "Definition of the glossary term",
                null, null, null, null,
                Collections.singletonList(relatedTerms), null
        );
        ManageGlossaryTermsGroups.createGlossaryTerm(glossaryTerms);
    }

    @Test
    void createGlossaryTerm1() throws Exception {
        String aa = ManageGlossaryTermsGroups.createGlossaryTerm("manisha","manish kumar gupta");
        System.out.println("return code " + aa);
    }
    private static byte[] serializeToBytes(DataTemplate<?> dataTemplate) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        return byteArrayOutputStream.toByteArray();
    }
    @Test
    void createGlossaryTerm2() throws IOException, ExecutionException, InterruptedException, URISyntaxException {

        // Urn glossaryTermKeyUrn = Urn.createFromString("urn:li:glossaryTerm:manish" );
        Urn glossaryTermKeyUrn = Urn.createFromString("urn:li:glossaryTerm:manishanuja" );

        GlossaryTermInfo termInfo = new GlossaryTermInfo()
                .setName("TESTGT")
                .setDefinition("MMAMXMD  DSD SDD sdsd SDSD ")
                .setTermSource("INTERNAL")
                ;

      ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(termInfo);
        // Serialize DataMap to JSON string

        // Convert JSON string to ByteString
        ByteString byteString1 = ByteString.unsafeWrap(jsonString.getBytes("UTF-8"));


        ByteString byteString = ByteString.copyAvroString(jsonString, true);  // Serialize to ByteString
         String DATAHUB_URL = "http://localhost:8080";
         String DATAHUB_AUTH_TOKEN = "";

         GenericAspect genericAspect = new GenericAspect();
        genericAspect.setValue(byteString1);  // Set the serialized aspect

        MetadataChangeProposal proposal = new MetadataChangeProposal();
        proposal.setEntityUrn(glossaryTermKeyUrn);
        proposal.setEntityType("glossaryTerm");
        proposal.setAspectName("glossaryTermInfo");
        proposal.setAspect(genericAspect);
        proposal.setChangeType(CREATE);

        genericAspect.setContentType("application/json");
        Emitter emitter = RestEmitter.create(b -> b
                .server(DATAHUB_URL) // Replace with your DataHub server URL
                .token(DATAHUB_AUTH_TOKEN)        // Replace with your token if required
        );
        Future<MetadataWriteResponse> response = emitter.emit(proposal, null);
        String returnCode = response.get().getResponseContent();
        if (returnCode.contains("success"))
        {System.out.println("Domain created successfully!");}
        else
        {System.out.println(returnCode);}
    }

    @Test
    void creareGlossaryTermKey() throws URISyntaxException, IOException, ExecutionException, InterruptedException {
        Urn glossaryTermKeyUrn = Urn.createFromString("urn:li:glossaryTerm:ManishWebWorld" );
        GlossaryTermKey gtk = new GlossaryTermKey().setName("GlossaryTermKeyName");


        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(gtk);
        ByteString byteString1 = ByteString.unsafeWrap(jsonString.getBytes("UTF-8"));


        ByteString byteString = ByteString.copyAvroString(jsonString, true);  // Serialize to ByteString
        String DATAHUB_URL = "http://localhost:8080";
        String DATAHUB_AUTH_TOKEN = "";

        GenericAspect genericAspect = new GenericAspect();
        genericAspect.setValue(byteString1);  // Set the serialized aspect

        MetadataChangeProposal proposal = new MetadataChangeProposal();
        proposal.setEntityUrn(glossaryTermKeyUrn);
        proposal.setEntityType("glossaryTerm");
        proposal.setAspectName("glossaryTermKey");
        proposal.setAspect(genericAspect);
        proposal.setChangeType(CREATE);
        genericAspect.setContentType("application/json");
        Emitter emitter = RestEmitter.create(b -> b
                .server(DATAHUB_URL) // Replace with your DataHub server URL
                .token(DATAHUB_AUTH_TOKEN)   );
        Future<MetadataWriteResponse> response = emitter.emit(proposal, null);
        String returnCode = response.get().getResponseContent();
        if (returnCode.contains("success"))
        {System.out.println("Domain created successfully!");}
        else
        {System.out.println(returnCode);}
    }

    @Test
    void creareGlossaryTermKeyTT() throws URISyntaxException, IOException, ExecutionException, InterruptedException {
        Urn glossaryTermKeyUrn = Urn.createFromString("urn:li:glossaryTerm:ManishWebWorld22e" );
        GlossaryTermKey gtk = new GlossaryTermKey().setName("Manish Web Worlds");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(gtk);
        ByteString byteString1 = ByteString.unsafeWrap(jsonString.getBytes("UTF-8"));
        String DATAHUB_URL = "http://localhost:8080";
        String DATAHUB_AUTH_TOKEN = "";

        GenericAspect genericAspect = new GenericAspect();
        genericAspect.setValue(byteString1);  // Set the serialized aspect

        MetadataChangeProposal proposal = new MetadataChangeProposal();
        proposal.setEntityUrn(glossaryTermKeyUrn);
        proposal.setEntityType("glossaryTerm");
        proposal.setAspectName("glossaryTermKey");
        proposal.setAspect(genericAspect);
        proposal.setChangeType(UPSERT);
        genericAspect.setContentType("application/json");
        Emitter emitter = RestEmitter.create(b -> b
                .server(DATAHUB_URL) // Replace with your DataHub server URL
                .token(DATAHUB_AUTH_TOKEN)   );
        Future<MetadataWriteResponse> response = emitter.emit(proposal, null);
        String returnCode = response.get().getResponseContent();
        if (returnCode.contains("success"))
        {System.out.println("Domain created successfully!");}
        else
        {System.out.println(returnCode);}

        //DataMap dm = new DataMap(10);

        Urn dataProductUrn = Urn.createFromString("urn:li:glossaryNode:30dd6e78-a83f-4d11-86a0-585f041976af");
        */
/*dm.put("term", "Data Engineering");
        dm.put("definition", "A field of engineering focused on data collection, management, and analysis.");
        dm.put("termSource", "INTERNAL");
        dm.put("sourceRef", "mmmmmmm");
        dm.put("name", "myvalue");
        dm.put("id", "XXXXXXXXXX");*//*

     //   dm.put("parentNode", dataProductUrn.toString());

        GlossaryTermInfo termInfo = new GlossaryTermInfo()
                .setName("CUSTOM_dataProductUrn")
                .setDefinition("MMAMXMD  DSD SDD sdsd SDSD ")
                .setTermSource("INTERNAL")
                .setSourceRef("INTERNAL-")
                ;


        StringMap MapCustomProperties = new StringMap();
        MapCustomProperties.put("NAME" , "Manish");
        termInfo.setCustomProperties(MapCustomProperties);

        String jsonStringtermInfo = objectMapper.writeValueAsString(termInfo);
        ByteString byteStringtermInfo = ByteString.unsafeWrap(jsonStringtermInfo.getBytes("UTF-8"));

        System.out.println(jsonStringtermInfo);

        GenericAspect genericAspecttermInfo = new GenericAspect();
        genericAspect.setValue(byteStringtermInfo);  // Set the serialized aspect

        MetadataChangeProposal proposaltermInfo = new MetadataChangeProposal();
        proposal.setEntityUrn(glossaryTermKeyUrn);
        proposal.setEntityType(GLOSSARY_TERM_ENTITY_NAME);
        proposal.setAspectName(GLOSSARY_TERMS_ASPECT_NAME);
        proposal.setAspect(genericAspecttermInfo);
        proposal.setChangeType(UPSERT);
        genericAspect.setContentType("application/json");

        Future<MetadataWriteResponse> responsetermInfo = emitter.emit(proposaltermInfo, null);
        String returnCodetermInfo = responsetermInfo.get().getResponseContent();
        if (returnCodetermInfo.contains("success"))
        {System.out.println("glossaryTermInfo created successfully!" + returnCodetermInfo);}
        else
        {System.out.println(returnCodetermInfo );}



    }
}*/
