package com.progressive.minds.chimera.repository;

import com.progressive.minds.chimera.entity.DataPipeline;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface PipelineRepository {

    @Select("SELECT EXISTS(SELECT 1 FROM data_pipelines WHERE id = #{id})")
    boolean isDataPipeLineExists(int id);

    @Select("SELECT * FROM data_pipelines WHERE id = #{id}")
    DataPipeline getPipelineById(int id);

    @Select("SELECT * FROM data_pipelines")
    List<DataPipeline> getAllPipelines();

    @Insert("INSERT INTO data_pipelines (ID, PIPELINE_NAME, PIPELINE_DESCRIPTION, PROCESS_MODE, TAGS, ORG_HIER_NAME, CREATED_TIMESTAMP, CREATED_BY, UPDATED_TIMESTAMP, UPDATED_BY, ACTIVE_FLAG) " +
            "VALUES (#{id}, #{pipelineName}, #{pipelineDescription}, #{processMode}, #{tags}, #{orgHierName}, #{createdTimestamp}, #{createdBy}, #{updatedTimestamp}, #{updatedBy}, #{activeFlag})")
    void insertPipeline(DataPipeline pipeline);

    @Update("UPDATE data_pipelines SET " +
            "PIPELINE_DESCRIPTION = #{pipelineDescription}, " +
            "PROCESS_MODE = #{processMode}, " +
            "TAGS = #{tags}, " +
            "ORG_HIER_NAME = #{orgHierName}, " +
            "UPDATED_TIMESTAMP = #{updatedTimestamp}, " +
            "UPDATED_BY = #{updatedBy}, " +
            "ACTIVE_FLAG = #{activeFlag} " +
            "WHERE PIPELINE_NAME = #{pipelineName}")
    void updatePipeline(int id, DataPipeline pipeline);
}
