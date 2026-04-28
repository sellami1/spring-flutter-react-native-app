package tn.sellami.students.rest_spring_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI studentsOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Students REST API")
                        .description("API documentation for students and departments endpoints")
                        .version("v2"));
    }
}
