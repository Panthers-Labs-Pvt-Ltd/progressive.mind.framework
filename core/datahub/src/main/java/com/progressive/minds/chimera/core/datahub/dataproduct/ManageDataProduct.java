package com.progressive.minds.chimera.core.datahub.dataproduct;

import com.progressive.minds.chimera.core.datahub.common.*;
import com.linkedin.common.AuditStamp;
import com.linkedin.common.DatasetUrn;
import com.linkedin.common.url.Url;
import com.linkedin.common.urn.CorpuserUrn;
import com.linkedin.common.urn.Urn;
import com.linkedin.data.DataMap;
import com.linkedin.data.template.StringMap;
import com.linkedin.dataproduct.DataProductAssociation;
import com.linkedin.dataproduct.DataProductAssociationArray;
import com.linkedin.mxe.GenericAspect;
import com.linkedin.mxe.MetadataChangeProposal;
import com.progressive.minds.chimera.core.datahub.SharedLogger;
import com.linkedin.dataproduct.DataProductKey;
import com.linkedin.dataproduct.DataProductProperties;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import com.linkedin.common.Ownership;

import static com.linkedin.data.template.SetMode.REMOVE_IF_NULL;
import static com.progressive.minds.chimera.core.datahub.common.genericUtils.*;
import com.linkedin.common.GlobalTags;
import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;
;

public class ManageDataProduct implements SharedLogger {
    ChimeraLogger DatahubLogger = ChimeraLoggerFactory.getLogger(SharedLogger.class);
    String LoggerTag = "[DataHub- Manage DataProduct] -";

    // Create a Data Product and associate it with a domain
    public String createDataProduct(String domainName, String dataProductName,
                                    String dataProductDescription, Map<String, String> customProperties) {
        try {
            DatahubLogger.logInfo("Creating Data Product " + dataProductName);

            // Create Data Product URN
            Urn dataProductUrn = Urn.createFromString("urn:li:dataProduct:" + replaceSpecialCharsAndLowercase(dataProductName));

            // Create the DataProductKey
            DataProductKey dataProductKey = new DataProductKey();
            dataProductKey.setId(replaceSpecialCharsAndLowercase(dataProductName));

            StringMap MapCustomProperties = new StringMap();
            MapCustomProperties.putAll(customProperties);

            AuditStamp createdStamp = new AuditStamp()
                    .setActor(new CorpuserUrn("data_creator"))
                    .setTime(Instant.now().toEpochMilli());    // Current timestamp in milliseconds

            Map<String, String> owner = new HashMap<>();
            owner.put("John Doe", "Data Creator");
            owner.put("Manu", "Data owner");

            ManageOwners.addOwners(dataProductUrn, "dataProduct", "ownership","UPSERT",
                    owner);


            Urn DSURN = Urn.createFromString("urn:li:chart:(looker,baz1)");
            //setSourceUrn(DSURN).
            DataProductAssociation dpa = new DataProductAssociation().setCreated(createdStamp)
                    .setLastModified(createdStamp).setDestinationUrn(DSURN);

            DataProductAssociationArray aa = new DataProductAssociationArray();
            aa.add(dpa);

            // Create the DataProductProperties
            DataProductProperties dataProductProperties = new DataProductProperties()
                    .setName(dataProductName)
                    .setAssets(aa)
                    .setDescription(dataProductDescription)
                    .setExternalUrl(new Url("http.yahoo.com"), REMOVE_IF_NULL)
                    .setCustomProperties(MapCustomProperties);

            System.out.println("DataProductProperties: " + dataProductProperties);
            System.out.println("MapCustomProperties DataMap: " + MapCustomProperties);

            MetadataChangeProposal proposal = createProposal(String.valueOf(dataProductUrn), "dataProduct",
                    "dataProductProperties", "UPSERT", dataProductProperties);

            System.out.println("proposal DataMap: " + dataProductProperties);
            String dataproductUrn =  emitProposal(proposal, "dataProduct");
            // String assetsUrn = addAssetsToDataProduct(Urn.createFromString(dataproductUrn));
            return dataproductUrn;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create data product", e);
        }
    }
/*
    public String addAssetsToDataProduct(Urn dataProductUrn) throws URISyntaxException, IOException, ExecutionException, InterruptedException {

        Urn DSURN = Urn.createFromString("urn:li:dataset:(urn:li:dataPlatform:postgres,obdef.public.edl_batch_log,PROD)");

        DataProductAssociation dpa = new DataProductAssociation().setSourceUrn(DSURN).setDestinationUrn(dataProductUrn);

        DataProductAssociationArray aa = new DataProductAssociationArray();
        aa.add(dpa);

        DataProductProperties dataProductProperties = new DataProductProperties()
                .setAssets(aa);

        MetadataChangeProposal proposal = createProposal(String.valueOf(dataProductUrn), "dataProduct",
                "dataProductProperties", "UPSERT", dataProductProperties);

        System.out.println("proposal DataMap: " + dataProductProperties);
        return emitProposal(proposal, "dataProduct");
    }*/
}