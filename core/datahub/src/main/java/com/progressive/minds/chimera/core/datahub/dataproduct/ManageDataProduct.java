package com.progressive.minds.chimera.core.datahub.dataproduct;

import com.linkedin.common.*;
import com.linkedin.common.url.Url;
import com.linkedin.common.urn.CorpuserUrn;
import com.linkedin.common.urn.GlossaryTermUrn;
import com.linkedin.common.urn.Urn;
import com.linkedin.data.template.StringMap;
import com.linkedin.dataproduct.DataProductAssociation;
import com.linkedin.dataproduct.DataProductAssociationArray;
import com.linkedin.dataproduct.DataProductKey;
import com.linkedin.dataproduct.DataProductProperties;
import com.linkedin.domain.Domains;
import com.linkedin.mxe.MetadataChangeProposal;
import com.progressive.minds.chimera.core.datahub.common.ManageGlobalTags;
import com.progressive.minds.chimera.core.datahub.common.ManageOwners;
import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;
import datahub.shaded.org.apache.commons.lang3.ArrayUtils;
import datahub.shaded.org.apache.commons.lang3.StringUtils;
import datahub.shaded.org.apache.commons.lang3.tuple.Pair;


import javax.validation.constraints.Null;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.linkedin.data.template.SetMode.REMOVE_IF_NULL;
import static com.progressive.minds.chimera.core.datahub.common.genericUtils.*;

public class ManageDataProduct {

    ChimeraLogger DatahubLogger = ChimeraLoggerFactory.getLogger(ManageDataProduct.class);
    String LoggerTag = "[DataHub- Create DataProduct] -";


    /**
     * @param dataProductName        Name of The Data Product which needs to be created
     * @param dataProductDescription Description of Data Product can be Long String or MD5 Document Content
     * @param externalURL            External URL which needs to be mapped
     * @param domainName             Valid domain with which this newly data product needs to be mapped
     * @param globalTags             Global tags array, which needs to be added with Data product
     * @param glossaryTerms          Glossary Terms array, which needs to be added with Data product
     * @param DataAssets             Data Assets <Name, Type and Platform> information in Map<String, Pair<String,String>>
     * @param Owners                 Owners Information in Map<ownerName, ownershipType>
     * @param customProperties       User Defined Properties in Map<PropertyName, PropertyValue>
     * @return                       URN of Data Product e.g. urn:li:dataProduct:DataProductName
     */
    public String createDataProduct(String dataProductName,
                                    String dataProductDescription,
                                    @Null String externalURL,
                                    @Null String domainName,
                                    @Null String[] globalTags,
                                    @Null String[] glossaryTerms,
                                    @Null Map<String, Pair<String, String>> DataAssets,
                                    @Null Map<String, String> Owners,
                                    @Null Map<String, String> customProperties) {
        String retVal;
        System.setProperty("CHIMERA_EXE_ENV", "datahub");
        try {
            DatahubLogger.logInfo(LoggerTag + "Creating Data Product " + dataProductName);

            Urn dataProductUrn = Urn.createFromString("urn:li:dataProduct:" + replaceSpecialCharsAndLowercase(dataProductName));

            DataProductKey dataProductKey = new DataProductKey().
                    setId(replaceSpecialCharsAndLowercase(dataProductName));
            DatahubLogger.logInfo(LoggerTag + "Adding Data Product With Key " + dataProductKey);

            DataProductProperties dataProductProperties = new DataProductProperties()
                    .setName(dataProductName)
                    .setDescription(dataProductDescription);
            DatahubLogger.logInfo(LoggerTag + "Setting Data Product Properties...");

            if (externalURL != null && !externalURL.isEmpty()) {
                DatahubLogger.logInfo(LoggerTag + "Setting External URL With Data Product...");
                dataProductProperties.setExternalUrl(new Url(externalURL), REMOVE_IF_NULL);
            }

            if (customProperties != null && !customProperties.isEmpty()) {
                DatahubLogger.logInfo(LoggerTag + "Setting User Defined Custom Properties With Data Product...");
                StringMap MapCustomProperties = new StringMap();
                MapCustomProperties.putAll(customProperties);
                dataProductProperties.setCustomProperties(MapCustomProperties);
            }

            DatahubLogger.logInfo(LoggerTag + "Setting Audit Information's With Data Product...");
            AuditStamp createdStamp = new AuditStamp()
                    .setActor(new CorpuserUrn("data_creator"))
                    .setTime(Instant.now().toEpochMilli());    // Current timestamp in milliseconds

            if (DataAssets != null && !DataAssets.isEmpty()) {
                DatahubLogger.logInfo(LoggerTag + "Mapping Assets With Data Product...");
                DataProductAssociationArray dataProductAssociationArray = new DataProductAssociationArray();
                for (Map.Entry<String, Pair<String, String>> entry : DataAssets.entrySet()) {
                    String name = entry.getKey();
                    Pair<String, String> typePlatformPair = entry.getValue();
                    String type = typePlatformPair.getLeft();     // Extract type
                    String platform = typePlatformPair.getRight(); // Extract platform

                    // Construct URN string
                    String urnString = String.format("urn:li:%s:(%s,%s)", type, platform, name);

                    // Create URN object
                    Urn urn = Urn.createFromString(urnString);

                    // Create DataProductAssociation
                    DataProductAssociation dataProductAssociation = new DataProductAssociation()
                            .setCreated(createdStamp)
                            .setLastModified(createdStamp)
                            .setDestinationUrn(urn);

                    // Add to the DataProductAssociationArray
                    dataProductAssociationArray.add(dataProductAssociation);
                    DatahubLogger.logInfo(LoggerTag + String.format("Assets Type %s, Platform Name %s and Assets Name %s mapped" +
                            " With Data Product.", type, platform, name));
                }
                dataProductProperties.setAssets(dataProductAssociationArray);
            }

            MetadataChangeProposal proposal = createProposal(String.valueOf(dataProductUrn), "dataProduct",
                    "dataProductProperties", "UPSERT", dataProductProperties);

            DatahubLogger.logInfo(LoggerTag + "Preparing for MetadataChangeProposal : " + proposal);

            retVal = emitProposal(proposal, "dataProduct");

            if (retVal.equalsIgnoreCase(dataProductUrn.toString())) {
                DatahubLogger.logInfo(LoggerTag + String.format("Data Product %s Created on Datahub With URN %s",
                        dataProductName, retVal));

                if (Owners != null && !Owners.isEmpty()) {
                    DatahubLogger.logInfo(LoggerTag + "Mapping Owners Information's with Data Product");
                    ManageOwners.addOwners(dataProductUrn, "dataProduct", "ownership", "UPSERT",
                            Owners);
                }

                if (domainName != null && !domainName.isEmpty()) {
                    DatahubLogger.logInfo(LoggerTag + "Mapping Data Product With Domain " + domainName);
                    String domainUrnString = "urn:li:domain:" + domainName;

                    UrnArray DomainUrn = new UrnArray();
                    DomainUrn.add(Urn.createFromString(domainUrnString));
                    Domains domains = new Domains().setDomains(DomainUrn);

                    MetadataChangeProposal domainProposal = createProposal(retVal,
                            "dataProduct", "domains", "UPSERT", domains);
                    String retval = emitProposal(domainProposal, "domains");
                    DatahubLogger.logInfo(LoggerTag + "Mapping Data Product With Domain Completed With " + retval);
                }

                if (globalTags != null &&  ArrayUtils.isNotEmpty(globalTags)) {
                    DatahubLogger.logInfo(LoggerTag + "Mapping Global Tags With Data Product");
                    String retval = ManageGlobalTags.addTags(Urn.createFromString(dataProductUrn.toString()),
                            "dataProduct", "UPSERT", globalTags);
                    DatahubLogger.logInfo(LoggerTag + "Mapping Global Tags With Data Product Completed With " + retval);
                } else {
                    DatahubLogger.logInfo("GlobalTags array is null or empty.");
                }

                if (glossaryTerms != null &&  ArrayUtils.isNotEmpty(glossaryTerms)) {
                    GlossaryTermAssociationArray glossaryTermAssociationArray = new GlossaryTermAssociationArray();

                    DatahubLogger.logInfo(LoggerTag + "Mapping Glossary Terms Tags Data Product");

                    for (String glossaryTerm : glossaryTerms) {
                        if (StringUtils.isNotBlank(glossaryTerm)) {
                            DatahubLogger.logInfo(LoggerTag + String.format("Mapping Glossary Term %s With Data Product", glossaryTerm));
                            GlossaryTermUrn glossaryTermUrn = GlossaryTermUrn.createFromString("urn:li:glossaryTerm:"+ glossaryTerm);
                            GlossaryTermAssociation termAssociation = new GlossaryTermAssociation()
                                    .setUrn(glossaryTermUrn);
                            glossaryTermAssociationArray.add(termAssociation);
                        }
                    }
                    GlossaryTerms glossaryTerm = new GlossaryTerms()
                            .setTerms(glossaryTermAssociationArray).setAuditStamp(createdStamp);
                    MetadataChangeProposal glossaryProposal = createProposal(dataProductUrn.toString(),
                            "dataProduct", "glossaryTerms", "UPSERT", glossaryTerm);
                    String retval = emitProposal(glossaryProposal, "glossaryTerms");
                    DatahubLogger.logInfo(LoggerTag + "Mapping Glossary Terms Tags Data Product Completed " + retval);
                } else {
                    System.out.println("Glossary Terms array is null or empty.");
                }
            } else {
                DatahubLogger.logError(" Data Product Creation Failed With Return :" + retVal);
            }
        } catch (Exception e) {
            DatahubLogger.logError(" Data Product Creation Failed ");
            throw new RuntimeException("Failed to create data product", e);
        }
        return retVal;
    }

    public void addAssetToDataProduct(String dataProductName, String assetsName) throws URISyntaxException, IOException, ExecutionException, InterruptedException {
        AuditStamp createdStamp = new AuditStamp()
                .setActor(new CorpuserUrn("data_creator"))
                .setTime(Instant.now().toEpochMilli());    // Current timestamp in milliseconds
        DataProductAssociationArray dataProductAssociationArray = new DataProductAssociationArray();
        DataProductAssociation dataProductAssociation = new DataProductAssociation()
                .setCreated(createdStamp)
                .setLastModified(createdStamp)
                .setDestinationUrn(Urn.createFromString(assetsName));
        Urn dataProductUrn = Urn.createFromString("urn:li:dataProduct:" + replaceSpecialCharsAndLowercase(dataProductName));
        // Add to the DataProductAssociationArray
        dataProductAssociationArray.add(dataProductAssociation);
        DataProductProperties dataProductProperties = new DataProductProperties()
                .setName(dataProductName)
                .setAssets(dataProductAssociationArray);

        MetadataChangeProposal proposal = createProposal(String.valueOf(dataProductUrn), "dataProduct",
                "dataProductProperties", "UPSERT", dataProductProperties);
        String retval = emitProposal(proposal, "dataProductProperties");


    }
}