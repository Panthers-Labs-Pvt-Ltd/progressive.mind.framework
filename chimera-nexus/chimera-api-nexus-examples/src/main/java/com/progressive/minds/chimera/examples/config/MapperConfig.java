package com.progressive.minds.chimera.examples.config;

import com.progressive.minds.chimera.examples.mapper.CustomDataPipelineMapper;
import com.progressive.minds.chimera.examples.mapper.generated.DataPipelineMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public CustomDataPipelineMapper customDataPipelineMapper(DataPipelineMapper dataPipelineMapper) {
        return new CustomDataPipelineMapper(dataPipelineMapper);
    }
}
