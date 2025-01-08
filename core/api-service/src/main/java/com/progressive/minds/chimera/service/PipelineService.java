package com.progressive.minds.chimera.service;


import com.progressive.minds.chimera.repository.PipelineRepository;
import com.progressive.minds.chimera.entity.Pipeline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PipelineService {

    @Autowired
    private PipelineRepository pipelineRepository;

    public List<Pipeline> getAllPipelines() {
        return pipelineRepository.getAllPipelines();
    }

    public void insertPipeline(Pipeline pipeline) {
        pipelineRepository.insertPipeline(pipeline);
    }

    public void updatePipeline(int id, Pipeline pipeline) {
        pipelineRepository.updatePipeline(id, pipeline);
    }
}
