package tn.sellami.students.gradingservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI gradingOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Grading Service API")
                        .description("API documentation for grading notes")
                        .version("v1"));
    }
}
