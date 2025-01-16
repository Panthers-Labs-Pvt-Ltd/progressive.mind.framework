package com.progressive.minds.chimera.core.datahubutils.dataproduct;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.linkedin.common.urn.Urn;
import com.linkedin.data.template.StringMap;
import com.linkedin.mxe.MetadataChangeProposal;
import com.progressive.minds.chimera.core.datahubutils.DataHubUtils;
import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;

import com.linkedin.dataproduct.DataProductKey;
import com.linkedin.dataproduct.DataProductProperties;
import java.util.Map;
import static com.progressive.minds.chimera.core.datahubutils.common.genericUtils.*;


public class ManageDataProduct implements DataHubUtils {

    private static final ChimeraLogger DatahubLogger = ChimeraLoggerFactory.getLogger(ManageDataProduct.class);
    String LoggerTag = "[DataHub- Manage DataProduct] -";
    //DatahubLogger.logInfo("Setting Console As Open Lineage Transport Type");

    // Create a Data Product and associate it with a domain
    public String createDataProduct(String domainName, String dataProductName,
                                    String dataProductDescription, Map<String, String> customProperties)  {
        try {
            // Create Data Product URN
            Urn dataProductUrn = Urn.createFromString("urn:li:dataProduct:" + dataProductName);

            // Create the DataProductKey
            DataProductKey dataProductKey = new DataProductKey();
            dataProductKey.setId(replaceSpecialCharsAndLowercase(dataProductName));

            StringMap MapCustomProperties = new StringMap();
            MapCustomProperties.putAll(customProperties);

           // Create the DataProductProperties
            DataProductProperties dataProductProperties = new DataProductProperties()
                    .setName(dataProductName)
                    .setDescription(dataProductDescription)
                    .setCustomProperties(MapCustomProperties);
            System.out.println("DataProductProperties: " + dataProductProperties);
            System.out.println("MapCustomProperties DataMap: " + MapCustomProperties);

            MetadataChangeProposal proposal = createProposal(String.valueOf(dataProductUrn), "dataProduct",
                    "dataProductProperties", "UPSERT",dataProductProperties);

            System.out.println("proposal DataMap: " + dataProductProperties);
            return emitProposal(proposal, "dataProduct");

           /* Future<MetadataWriteResponse> response = getEmitter().emit(proposal, null);
            String returnCode = response.get().getResponseContent();
            if (returnCode.contains("success"))
            {System.out.println("Domain created successfully!");}
            else
            {System.out.println(returnCode);}*/
            //return returnCode;
            //return emitProposal(proposal, "dataProduct");
            // Emit the proposal to DataHub (assuming the emitProposal method exists)
            //return SampleEmitter(proposal, "dataProduct");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create data product", e);
        }
    }
}
