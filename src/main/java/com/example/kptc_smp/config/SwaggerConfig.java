package com.example.kptc_smp.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityScheme;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${server.url}")
    private String serverUrl;

    @Bean
    public OpenAPI customOpenAPI() {
        final String secSchemeName = "bearerAuth";
        return new OpenAPI()
                .info(
                        new Info()
                                .title("KPTC_SMP")
                                .version("v 1.0")
                )
                .components(new Components()
                        .addSecuritySchemes(secSchemeName,
                                new SecurityScheme()
                                        .name(secSchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .servers(List.of(new Server().url(serverUrl)));
    }
}