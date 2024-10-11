package in.arbii.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class SwaggerConfig {

    @Bean
    public GroupedOpenApi customerApi() {
        return GroupedOpenApi.builder()
                .group("ApplicationRegistration")
                .displayName("Application Registration API")
                .packagesToScan("in.arbii.rest")
                .build();
    }
}
