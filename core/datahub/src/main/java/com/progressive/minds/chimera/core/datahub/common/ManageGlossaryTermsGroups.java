package com.progressive.minds.chimera.core.datahub.common;

// import com.linkedin.common.GlossaryNodeUrn;
import com.linkedin.common.Status;
import com.linkedin.common.url.Url;
import com.linkedin.common.urn.Urn;
import com.linkedin.data.ByteString;
import com.linkedin.data.template.StringMap;
import com.linkedin.glossary.GlossaryTermInfo;
import com.linkedin.metadata.key.GlossaryTermKey.Fields;
import com.linkedin.mxe.GenericAspect;
import com.linkedin.mxe.MetadataChangeProposal;

import static com.progressive.minds.chimera.core.datahub.common.genericUtils.*;
import static com.progressive.minds.chimera.core.datahub.common.genericUtils.replaceSpecialCharsAndLowercase;

import com.linkedin.glossary.GlossaryNodeInfo;

import com.linkedin.glossary.GlossaryRelatedTerms;

import com.linkedin.common.urn.GlossaryNodeUrn;
import com.progressive.minds.chimera.core.datahub.domain.ManageDomain;
import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;
import datahub.shaded.jackson.databind.ObjectMapper;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.net.URL;
import java.util.List;
import java.util.Map;

// TODO - Created Test Cases for Basic Use case but seems some issue in schema
// In case of any null values in optional field throwing Internal Server Error
// Node URN is falling with Invalid Number, see Test Case of more
/*

curl -X 'POST' \
        'http://localhost:8080/openapi/v3/entity/glossaryterm/urn%3Ali%3AglossaryTerm%3AManishWebWorld/glossaryterminfo?async=false&systemMetadata=false&createIfEntityNotExists=false&createIfNotExists=false' \
        -H 'accept: application/json' \
        -H 'Content-Type: application/json' \
        -d '{
        "value": {
        "name": "CUSTOM_dataProductUrn",
        "id": "manishKumarGuptaId",
        "definition": "MMAMXMD DSD SDD sdsd SDSD",
        "termSource": "INTERNAL",
        "sourceRef": "INTERNAL-",
        "rawSchema": "",
        "parentNode": "urn:li:glossaryNode:9c46e657-552d-4b30-9de7-ec87aba95410",
        "sourceUrl": "http://yaho.com",
        "customProperties": {
        "NAME": "Manish"
        }
        },
        "systemMetadata": {},
        "headers": {
        "additionalProp1": "MANISH",
        "additionalProp2": "KUMAR",
        "additionalProp3": "Gupta"
        }
        }'*/
/**
 * GlossaryTermInfo, GlossaryNodeInfo, and GlossaryRelatedTerms: Relations and Usage
 * In DataHub, GlossaryTermInfo, GlossaryNodeInfo, and GlossaryRelatedTerms are used to manage and associate glossary terms, nodes, and relationships.
 *
 * GlossaryTermInfo:
 * Represents detailed information about a specific glossary term.
 * Contains fields such as name, description, and other metadata.
 *
 * GlossaryNodeInfo:
 * Represents a node in a glossary, which could be a standalone term or part of a term hierarchy.
 * Useful for managing and categorizing glossary terms into structured hierarchies.
 *
 * GlossaryRelatedTerms:
 * Represents relationships between glossary terms.
 * Used to establish links between different glossary terms, such as related terms or synonyms.
 *
 * Relations:
 * GlossaryTermInfo provides the structure for a glossary term.
 * GlossaryNodeInfo organizes these terms into a hierarchy.
 * GlossaryRelatedTerms establishes relationships between terms.
 */
public class ManageGlossaryTermsGroups {
    private static final ChimeraLogger DatahubLogger = ChimeraLoggerFactory.getLogger(ManageDomain.class);

    public record GlossaryNodeRecord(@NotNull String name, @NotNull String definition, @Null String parentTermName,
                                     @Null String sourceRef,@Null  String sourceURL, @Null  Map<String, String> customProperties) {    }

    public record GlossaryRelatedTermsRecord(@NotNull String name, @NotNull String definition, @Null String parentTermName,
                                             @Null String sourceRef, @Null String sourceURL, @Null Map<String, String> customProperties) {    }

    public record GlossaryTermsRecord(String name, String definition, @Null String parentTermName,
                                      @Null String sourceRef, @Null String sourceURL,@Null GlossaryNodeRecord glossaryNodeRecord,
                                      @Null List<GlossaryRelatedTermsRecord> glossaryRelatedTermsRecord,
                                      @Null  Map<String, String> customProperties) {    }
/*
    public static String createGlossaryTerm(String name, String definition) throws Exception {

        String UrnName = replaceSpecialCharsAndLowercase(name);
        Urn glossaryTermKeyUrn = Urn.createFromString("urn:li:glossaryTerm:" + UrnName);
        DatahubLogger.logInfo("Creating GlossaryTerm With URN " + glossaryTermKeyUrn);
        GlossaryTermInfo termInfo = new GlossaryTermInfo()
                .setId(replaceSpecialCharsAndLowercase(String.valueOf(glossaryTermKeyUrn)))
                .setName(name)
                .setDefinition(definition);

        GenericAspect genericAspect = serializeAspect(termInfo);
        DatahubLogger.logInfo("GlossaryTermInfo  GenericAspect " + genericAspect);

        MetadataChangeProposal proposal = createProposal(String.valueOf(glossaryTermKeyUrn), "glossaryTerm",
                "glossaryTermInfo", "CREATE", genericAspect);
        DatahubLogger.logInfo("GlossaryTermInfo  MetadataChangeProposal " + proposal);

        return emitProposal(proposal, "glossaryTermProperties");
    }*/


    public static String createGlossaryTerm(String name, String definition) throws Exception {

        // String UrnName = replaceSpecialCharsAndLowercase(name);
        Urn glossaryTermKeyUrn = Urn.createFromString("urn:li:glossaryTerm:" + name);
        // DatahubLogger.logInfo("Creating GlossaryTerm With URN " + glossaryTermKeyUrn);

        GlossaryTermInfo termInfo = new GlossaryTermInfo()
                .setName(name)
                .setDefinition("MMAMXMD  DSD SDD sdsd SDSD ")
                .setTermSource("INTERNAL")
                ;


        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(termInfo);

        // GenericAspect genericAspect = termInfo; //serializeAspect(termInfo);
        DatahubLogger.logInfo("GlossaryTermInfo  GenericAspect " + jsonString);
        ByteString byteString = ByteString.copyAvroString(jsonString, true);  // Serialize to ByteString

        GenericAspect genericAspect = new GenericAspect();
        genericAspect.setValue(byteString);  // Set the serialized aspect

        MetadataChangeProposal proposal = createProposal(String.valueOf(glossaryTermKeyUrn), "glossaryTerm",
                "glossaryTermInfo", "CREATE", genericAspect);
        DatahubLogger.logInfo("GlossaryTermInfo  MetadataChangeProposal " + proposal);

        return emitProposalRest(proposal, "glossaryTermInfo");
    }


    public static String createGlossaryTerm(GlossaryTermsRecord glossaryTerms) throws Exception {

        DatahubLogger.logInfo("Creating GlossaryTerm With Name " + glossaryTerms.name());
        if (glossaryTerms.name() == null || glossaryTerms.name().isEmpty()) {
            return "Glossary term name is required.";
        }
        if (glossaryTerms.definition() == null || glossaryTerms.definition().isEmpty()) {
            return "Glossary term definition is required.";
        }

        String UrnName = replaceSpecialCharsAndLowercase(glossaryTerms.name());
        Urn glossaryTermKeyUrn = Urn.createFromString("urn:li:glossaryTerm:" + UrnName);
        DatahubLogger.logInfo("Creating GlossaryTerm With URN " + glossaryTermKeyUrn);
        GlossaryTermInfo termInfo = new GlossaryTermInfo()
                .setId(replaceSpecialCharsAndLowercase(String.valueOf(glossaryTermKeyUrn)))
                .setName(glossaryTerms.name())
                .setDefinition(glossaryTerms.definition());
       /* DatahubLogger.logInfo("GlossaryTermInfo  " + termInfo);
        if (glossaryTerms.customProperties != null && glossaryTerms.customProperties.size() >0) {
            StringMap MapCustomProperties = new StringMap();
            MapCustomProperties.putAll(glossaryTerms.customProperties);
            termInfo.setCustomProperties(MapCustomProperties);
        }

        if (glossaryTerms.sourceRef() != null) {
            termInfo.setSourceRef(glossaryTerms.sourceRef());
        }

        if (glossaryTerms.sourceURL() != null) {
            termInfo.setSourceUrl(new Url(glossaryTerms.sourceURL()));
        }

        if (glossaryTerms.parentTermName() != null) {
            // createGlossaryNode()
            termInfo.setParentNode(GlossaryNodeUrn.createFromString(glossaryTerms.parentTermName()));
        }

        if (glossaryTerms.sourceURL() != null) {
            termInfo.setSourceUrl(new Url(glossaryTerms.sourceURL()));
        }*/
/*
        if (glossaryTerms.glossaryRelatedTermsRecord() != null) {
            GlossaryRelatedTermsRecord relatedTerms = glossaryTerms.glossaryRelatedTermsRecord();

            if (relatedTerms.name() != null && !relatedTerms.name().isEmpty() &&
                    relatedTerms.definition() != null && !relatedTerms.definition().isEmpty()) {
            }*/
        /*if (glossaryTerms.glossaryRelatedTermsRecord() != null) {
            @Null List<GlossaryRelatedTermsRecord> relatedTerms = glossaryTerms.glossaryRelatedTermsRecord();
            if (relatedTerms.get(0).name() != null && !relatedTerms.get(0).name().isEmpty()) {
                // Do something with the related term (optional, you can save it separately)
                System.out.println("Related Term: " + relatedTerms.get(0).name());
            }
        }*/

            GenericAspect genericAspect = serializeAspect(termInfo);
        DatahubLogger.logInfo("GlossaryTermInfo  GenericAspect " + genericAspect);

        MetadataChangeProposal proposal = createProposal(String.valueOf(glossaryTermKeyUrn), "glossaryTerm",
                    "glossaryTermInfo", "CREATE", genericAspect);
        DatahubLogger.logInfo("GlossaryTermInfo  MetadataChangeProposal " + proposal);

        return emitProposal(proposal, "glossaryTermProperties");
/*
        // Create MetadataChangeProposal
        MetadataChangeProposal metadataChangeProposal = new MetadataChangeProposal();

        // Set the relevant metadata
        metadataChangeProposal.setEntityUrn(glossaryTermUrn); // Set the glossary term URN
        metadataChangeProposal.setEntityType("glossaryTerm");  // Entity type as "glossaryTerm"
        metadataChangeProposal.setAspectName("glossaryTermProperties"); // Aspect name
        metadataChangeProposal.setAspect(genericAspect); // Attach the GenericAspect
        metadataChangeProposal.setChangeType(MetadataChangeProposal.MetadataChangeProposalChangeType.CREATE); // Set Change Type as CREATE

        return metadataChangeProposal;

        GlossaryNodeInfo gni = new GlossaryNodeInfo();
        gni.setId();
        gni.setDefinition();
        gni.setCustomProperties();
        gni.setParentNode();

        GlossaryRelatedTerms grt = new GlossaryRelatedTerms();
        grt.setHasRelatedTerms();
        grt.setIsRelatedTerms();
        grt.setValues();
        */

    }

    public String createGlossaryNode(GlossaryNodeRecord glossaryNodeRecord, String name,String definition,
                                     @Null String parentNode, @Null Map<String, String> customProperties )
            throws Exception {

        StringMap MapCustomProperties = new StringMap();


        GlossaryNodeInfo gni = new GlossaryNodeInfo();
        gni.setId(replaceSpecialCharsAndLowercase(name));
        gni.setName(name);
        gni.setDefinition(definition);

        if (customProperties.size() > 0) {
            MapCustomProperties.putAll(customProperties);
            gni.setCustomProperties(MapCustomProperties);
        }
        if (parentNode  != null) {
          /*
            GlossaryNodeUrn currentNodeUrn = new GlossaryNodeUrn();
            GlossaryNodeUrn parentNodeUrn = new GlossaryNodeUrn();
            GlossaryNodeInfo gni = new GlossaryNodeInfo();
            gni.setParentNode(parentNodeUrn);  // Make sure the parent node is set here
            */
            GlossaryNodeUrn nodeUrn = new GlossaryNodeUrn(parentNode);
            gni.setParentNode(nodeUrn);
        }
        return "";
    }

}