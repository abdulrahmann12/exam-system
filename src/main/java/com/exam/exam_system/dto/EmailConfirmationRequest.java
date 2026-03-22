package com.exam.exam_system.dto;

import com.exam.exam_system.config.ValidationMessages;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmailConfirmationRequest {

	@Email(message = ValidationMessages.EMAIL_INVALID)
	@NotBlank(message = ValidationMessages.EMAIL_REQUIRED)
	private String email;
	
	@NotBlank(message = ValidationMessages.CONFIRMATION_CODE_REQUIRED)
	private String confirmationCode;
}