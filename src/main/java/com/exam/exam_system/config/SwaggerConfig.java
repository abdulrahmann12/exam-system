package com.exam.exam_system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Online Exam System API")
                .version("1.0")
                .description(
                    "RESTful API for the Online Exam System.\n\n" +
                    "This API provides endpoints for:\n" +
                    "- **Authentication** — Login, logout, password reset, and token refresh.\n" +
                    "- **User Management** — Create, update, activate/deactivate user accounts.\n" +
                    "- **Student Management** — Register students, manage profiles, and track enrollment.\n" +
                    "- **Organizational Structure** — Manage colleges, departments, and subjects.\n" +
                    "- **Exam Lifecycle** — Create exams, manage questions and choices, upload question files (PDF/CSV).\n" +
                    "- **Exam Sessions** — QR-based exam access, student identity verification, timed sessions, and answer submission.\n" +
                    "- **Results & Grading** — Automatic grading and result retrieval.\n" +
                    "- **Role & Permission Management** — Fine-grained access control with roles and permissions.\n\n" +
                    "All endpoints return a standard response envelope with `message` and `data` fields."
                )
                .contact(new Contact()
                    .name("Abdulrahman Ahmed")
                    .email("abdulraman.ahmedd@gmail.com")
                )
            );
    }
}
