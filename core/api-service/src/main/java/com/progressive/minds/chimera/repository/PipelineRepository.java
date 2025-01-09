package com.progressive.minds.chimera.repository;

import com.progressive.minds.chimera.entity.DataPipeline;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface PipelineRepository {

    @Select("SELECT * FROM pipelines WHERE id = #{id}")
    DataPipeline getPipelineById(int id);

    @Select("SELECT * FROM pipelines")
    List<DataPipeline> getAllPipelines();

    @Insert("INSERT INTO pipelines(name, created_date, last_modified_date, frequency, schedule) VALUES(#{name}, #{createdDate}, #{lastModifiedDate}, #{frequency}, #{schedule})")
    void insertPipeline(DataPipeline pipeline);

    @Update("UPDATE pipelines SET name = #{name}, created_date = #{createdDate}, last_modified_date = #{lastModifiedDate}, frequency = #{frequency}, schedule = #{schedule} WHERE id = #{id}")
    void updatePipeline(int id, DataPipeline pipeline);
}
