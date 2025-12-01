package com.example.fitness_tracking.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Fitness Tracking API",
        version = "1.0",
        description = """
            ## Система фітнес відстеження з JWT аутентифікацією
            
            ### Доступні ролі:
            - **ADMIN** - повний доступ до всіх функцій
            - **TRAINER** - доступ до тренерських функцій та перегляду користувачів
            - **USER** - обмежений доступ, тільки свої дані
            
            ### Тестові користувачі:
            - **admin** / **admin123** - повний доступ
            - **trainer** / **trainer123** - тренерські функції
            - **user** / **user123** - базовий доступ
            """
    ),
    security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer",
    description = "JWT Authorization header using the Bearer scheme. Example: 'Bearer {token}'"
)
public class OpenApiConfig {
}