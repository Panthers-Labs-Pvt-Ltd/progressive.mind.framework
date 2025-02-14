package com.progressive.minds.chimera.core.workflows.activities;

import com.progressive.minds.chimera.dto.PersistMetadata;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface PersistDataActivity {
    @ActivityMethod
    public void persistData(PersistMetadata config) throws Exception;

}
