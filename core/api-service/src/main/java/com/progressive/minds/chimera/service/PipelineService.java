package com.progressive.minds.chimera.service;


import com.progressive.minds.chimera.entity.DataPipeline;
import com.progressive.minds.chimera.repository.PipelineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PipelineService {

    @Autowired
    private PipelineRepository pipelineRepository;

    public boolean isDataPipeLineExists(int id) {
        return pipelineRepository.isDataPipeLineExists(id);
    }

    public DataPipeline getDataPipeLineById(int id) {
        return pipelineRepository.getPipelineById(id);
    }

    public List<DataPipeline> getAllPipelines() {
        return pipelineRepository.getAllPipelines();
    }

    public void insertPipeline(DataPipeline pipeline) {
        pipelineRepository.insertPipeline(pipeline);
    }

    public void updatePipeline(int id, DataPipeline pipeline) {
        pipelineRepository.updatePipeline(id, pipeline);
    }
}
