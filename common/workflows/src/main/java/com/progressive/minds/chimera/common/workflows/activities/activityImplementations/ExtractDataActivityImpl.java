package com.progressive.minds.chimera.common.workflows.activities.activityImplementations;

import com.progressive.minds.chimera.common.workflows.activities.ExtractDataActivity;
import com.progressive.minds.chimera.dto.ExtractMetadata;

public class ExtractDataActivityImpl implements ExtractDataActivity {
     @Override
    public void extractData (ExtractMetadata config) {
        System.out.println("Extract Data from " + config.getExtractSourceType() + "." + config.getExtractSourceSubType());
    }

}
