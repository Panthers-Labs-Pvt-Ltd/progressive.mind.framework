package com.progressive.minds.chimera.core.datalineage.facets;

import io.openlineage.client.OpenLineageClientUtils;
import org.junit.jupiter.api.Test;
import za.co.absa.cobrix.spark.cobol.utils.SparkUtils;

import java.net.URI;

class OpenLineageCustomTest {
    @Test
    public void CheckFacet()
    {
    CustomFacet.FileJobFacet event = new CustomFacet.FileJobFacet(URI.create("google.com"),"myfilename.txt",
            "Text","txt","|","'","1GB","GZIP");
    System.out.println(SparkUtils.prettyJSON(OpenLineageClientUtils.toJson(event)));

    }
}