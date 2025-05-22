package com.meudroz.backend_test_java.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Empresas - Desafio Droz")
                        .version("1.0.0")
                        .description("Documentação da API REST para cadastro e consulta de empresas. Desenvolvido com Spring Boot e documentado com Swagger/OpenAPI 3."));
    }
}
