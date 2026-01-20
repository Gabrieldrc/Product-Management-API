package com.hackerrank.sample.config;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Value("${info.app.version:unknown}")
    private String appVersion;

    @Bean
    public OpenAPI productApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Product API Service")
                        .description("API para la gestión de productos y stock. Incluye control de cuotas por IP.")
                        .version(appVersion)
                        .contact(new Contact().name("Support Team").email("api-support@meli.com")));
    }
}