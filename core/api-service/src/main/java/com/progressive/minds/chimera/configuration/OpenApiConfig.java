package com.progressive.minds.chimera.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Service API")
                .version("1.0")
                .description("Documentation for Chimera API")
                .contact(new Contact()
                    .name("Support")
                    .email("support@chimera_api.com")));
    }
}
