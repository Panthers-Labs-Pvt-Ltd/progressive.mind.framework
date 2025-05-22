package com.progressive.minds.chimera.examples.config;

import com.progressive.minds.chimera.examples.mapper.CustomDataPipelineMapper;
import com.progressive.minds.chimera.examples.mapper.CustomMetaDataPipelineMapper;
import com.progressive.minds.chimera.examples.mapper.generated.DataPipelineMapper;
import com.progressive.minds.chimera.examples.mapper.generated.MetaDataPipelineMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public CustomDataPipelineMapper customDataPipelineMapper(DataPipelineMapper dataPipelineMapper) {
        return new CustomDataPipelineMapper(dataPipelineMapper);
    }

    @Bean
    public CustomMetaDataPipelineMapper customMetaDataPipelineMapper(MetaDataPipelineMapper metaDataPipelineMapper) {
        return new CustomMetaDataPipelineMapper(metaDataPipelineMapper);
    }

}
