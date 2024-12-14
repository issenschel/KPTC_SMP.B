package com.example.kptc_smp.config;

import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        final String secSchemeName = "bearerAuth";
        return new OpenAPI()
                .info(
                        new Info()
                                .title("KPTC_SMP")
                                .version("v 1.0")

                )
                .addSecurityItem(new SecurityRequirement().addList(secSchemeName))
                .components(new Components()
                        .addSecuritySchemes(secSchemeName,
                                new SecurityScheme()
                                        .name(secSchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}