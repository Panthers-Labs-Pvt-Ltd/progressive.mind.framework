package com.progressive.minds.chimera.common.workflows.activities;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

import com.progressive.minds.chimera.dto.ExtractMetadata;

@ActivityInterface
public interface ExtractDataActivity {
    @ActivityMethod
    public void extractData(ExtractMetadata config);

}
