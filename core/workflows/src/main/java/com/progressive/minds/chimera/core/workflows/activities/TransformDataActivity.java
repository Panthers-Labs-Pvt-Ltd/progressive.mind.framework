package com.progressive.minds.chimera.core.workflows.activities;

import com.progressive.minds.chimera.core.api_service.dto.TransformMetadataConfig;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface TransformDataActivity {
    @ActivityMethod
    public void transformData(TransformMetadataConfig config) throws Exception;

}
