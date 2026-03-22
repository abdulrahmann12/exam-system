package com.exam.exam_system.dto;

import com.exam.exam_system.config.ValidationMessages;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDTO {

    @NotBlank(message = ValidationMessages.USERNAME_OR_EMAIL_REQUIRED)
	private String usernameOrEmail;
    
    @NotBlank(message = ValidationMessages.PASSWORD_REQUIRED)
    private String password;
}