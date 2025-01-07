package com.progressive.minds.chimera.core.databaseOps.model.metadata;

import com.progressive.minds.chimera.foundational.logging.LogKey.LogKey;

import java.sql.Timestamp;

public record dataPipelineeModel(String pipelineName,
                                  String pipelineDescription,
                                  String processMode,
                                  String runFrequency,
                                  Timestamp createdTimestamp,
                                  String createdBy,
                                  Timestamp updatedTimestamp,
                                  String updatedBy,
                                  String activeFlag) {}