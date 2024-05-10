package com.nri.ems.Config.JWT;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

// http://localhost:8082/swagger-ui/index.html

@OpenAPIDefinition(
        info = @io.swagger.v3.oas.annotations.info.Info(
                title = "Employee Management System Backend API",
                version = "1.0",
                description = "API Documentation for all REST Endpoints",
                contact = @io.swagger.v3.oas.annotations.info.Contact(
                        name = "NRI Fintech",
                        email = ""
                )
        ),
        servers = {
                @io.swagger.v3.oas.annotations.servers.Server(
                        url = "http://localhost:8082",
                        description = "Local server"
                )
        },
        security = {
                @io.swagger.v3.oas.annotations.security.SecurityRequirement(
                        name = "BearerAuth"
                )
        }
)
@SecurityScheme(
        name = "BearerAuth",
        description = "JWT Bearer Token",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)

public class OpenApiConfig {
    
}
