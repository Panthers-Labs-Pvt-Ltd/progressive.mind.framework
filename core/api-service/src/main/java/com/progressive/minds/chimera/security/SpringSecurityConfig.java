package com.progressive.minds.chimera.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Profile({"prod", "local"})
public class SpringSecurityConfig {


  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        // Disable CSRF (enable it in production if needed)
        .csrf(AbstractHttpConfigurer::disable)

        // Configure authorization rules
        .authorizeHttpRequests(auth -> auth
            // Permit actuator health check
            .requestMatchers("/actuator/health", "/actuator/info").permitAll()

            // Permit Swagger UI & API Docs
            .requestMatchers(
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/v3/api-docs/**",
                "/v3/api-docs.yaml"
            ).permitAll()

            // Role-based authorization
            .requestMatchers("/api/v1/pipelines/**").hasRole("chimera_admin")
            .requestMatchers("/user/**").hasRole("chimera_user")

            // authentication for everything else
            .anyRequest().authenticated()
        )

        //  OAuth2 Resource Server with JWT converter
        .oauth2ResourceServer(oauth2 -> oauth2.jwt(
            jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())
        ));

    return http.build();
  }


  @Bean
  public JwtAuthenticationConverter jwtAuthenticationConverter() {
    JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
    converter.setJwtGrantedAuthoritiesConverter(jwt -> new KeycloakRoleConverter().convertRoles(jwt));
    return converter;
  }
}
