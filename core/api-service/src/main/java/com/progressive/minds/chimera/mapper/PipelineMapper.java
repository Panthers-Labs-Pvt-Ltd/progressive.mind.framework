package com.progressive.minds.chimera.mapper;

import com.progressive.minds.chimera.model.Pipeline;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface PipelineMapper {

    @Select("SELECT * FROM pipelines WHERE id = #{id}")
    Pipeline getPipelineById(int id);

    @Select("SELECT * FROM pipelines")
    List<Pipeline> getAllPipelines();

    @Insert("INSERT INTO pipelines(name, created_date, last_modified_date, frequency, schedule) VALUES(#{name}, #{createdDate}, #{lastModifiedDate}, #{frequency}, #{schedule})")
    void insertPipeline(Pipeline pipeline);

    @Update("UPDATE pipelines SET name = #{name}, created_date = #{createdDate}, last_modified_date = #{lastModifiedDate}, frequency = #{frequency}, schedule = #{schedule} WHERE id = #{id}")
    void updatePipeline(int id, Pipeline pipeline);
}
