package com.progressive.minds.chimera.service;


import com.progressive.minds.chimera.mapper.PipelineMapper;
import com.progressive.minds.chimera.model.Pipeline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PipelineService {

    @Autowired
    private PipelineMapper pipelineMapper;

    public List<Pipeline> getAllPipelines() {
        return pipelineMapper.getAllPipelines();
    }

    public void insertPipeline(Pipeline pipeline) {
        pipelineMapper.insertPipeline(pipeline);
    }

    public void updatePipeline(int id, Pipeline pipeline) {
        pipelineMapper.updatePipeline(id, pipeline);
    }
}
