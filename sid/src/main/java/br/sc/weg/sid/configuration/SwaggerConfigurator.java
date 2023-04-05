package br.sc.weg.sid.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfigurator {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("API SEED - Springdoc OpenAPI")
                        .version("1.0.1")
                        .description("API SEED - Projeto solicitado pela empresa WEG S.A"));
    }


}
