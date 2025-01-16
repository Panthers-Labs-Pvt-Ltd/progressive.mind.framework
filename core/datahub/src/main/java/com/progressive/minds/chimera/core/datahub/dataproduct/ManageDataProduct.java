package com.progressive.minds.chimera.core.datahub.dataproduct;

import com.linkedin.common.urn.Urn;
import com.linkedin.data.template.StringMap;
import com.linkedin.mxe.MetadataChangeProposal;
import com.progressive.minds.chimera.core.datahub.SharedLogger;
import com.linkedin.dataproduct.DataProductKey;
import com.linkedin.dataproduct.DataProductProperties;
import java.util.Map;
import static com.progressive.minds.chimera.core.datahub.common.genericUtils.*;
import com.linkedin.common.GlobalTags;

public class ManageDataProduct implements SharedLogger {

    String LoggerTag = "[DataHub- Manage DataProduct] -";
    //DatahubLogger.logInfo("Setting Console As Open Lineage Transport Type");

    // Create a Data Product and associate it with a domain
    public String createDataProduct(String domainName, String dataProductName,
                                    String dataProductDescription, Map<String, String> customProperties)  {
        try {
            // Create Data Product URN
            Urn dataProductUrn = Urn.createFromString("urn:li:dataProduct:" + replaceSpecialCharsAndLowercase(dataProductName));

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
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create data product", e);
        }
    }
}
