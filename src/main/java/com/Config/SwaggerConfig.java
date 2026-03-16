package com.Config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI hotelOpenAPI() {

        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()

                // 🔐 Security configuration (JWT ready)
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))

                // 📌 API Information
                .info(new Info()
                        .title("Hotel Management API")
                        .description("""
                                REST API for Hotel Management System.
                                Supports role-based access (Admin, Staff, User).
                                Built with Spring Boot & Layered Architecture.
                                """)
                        .version("1.0")
                        .contact(new Contact()
                                .name("Brain404 Team - Ahmed | Habeeb | Najwan")
                                .email("brain404@gmail.com")
                                .url("https://github.com/Brain404"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))

                // 🌍 Servers
                .servers(List.of(
                        new Server().url("http://localhost:8080")
                                .description("Local Development Server"),
                        new Server().url("https://api.hotel-system.com")
                                .description("Production Server (Future)")
                ))

                // 🏷 Tags for grouping
                .tags(List.of(
                        new Tag().name("Amenity").description("Amenity Management APIs"),
                        new Tag().name("Booking").description("Booking Management APIs"),
                        new Tag().name("Room").description("Room Management APIs"),
                        new Tag().name("Payment").description("Payment APIs"),
                        new Tag().name("Review").description("Review APIs")
                ))

                // 📘 External Documentation
                .externalDocs(new ExternalDocumentation()
                        .description("Project GitHub Repository")
                        .url("https://github.com/Brain404/HotelSystem"));
    }
}