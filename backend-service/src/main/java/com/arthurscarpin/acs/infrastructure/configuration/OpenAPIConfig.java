package com.arthurscarpin.acs.infrastructure.configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT",
    description = "JWT token for authentication"
)
public class OpenAPIConfig {

    @Bean
    public OpenAPI getOpenAPI() {
        Contact contact = new Contact();
        contact.setName("Arthur Scarpin");
        contact.setEmail("scarpinarthur.dev@gmail.com");

        Info info = new Info();
        info.setTitle("Access Control System API");
        info.setVersion("v1");
        info.setDescription("Application to manage access events, owners, vehicles, and users for an access control system.");
        info.setContact(contact);
        return new OpenAPI().info(info);
    }
}
