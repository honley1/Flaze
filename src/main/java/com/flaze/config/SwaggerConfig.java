package com.flaze.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .servers(
                        List.of(
                                new Server().url("http://localhost:8080"),
                                new Server().url("https://696b-2-133-130-122.ngrok-free.app")
                        )
                )
                .info(
                        new Info().
                                title("Flaze - api for create and read articles")
                );
    }
}
