package com.exam.exam_system.dto;

import com.exam.exam_system.config.ValidationMessages;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPasswordRequestDTO {

	@Email(message = ValidationMessages.EMAIL_INVALID)
	@NotBlank(message = ValidationMessages.EMAIL_REQUIRED)
	private String email;

	@NotBlank(message = ValidationMessages.RESET_CODE_REQUIRED)
	private String code;

	@NotBlank(message = ValidationMessages.NEW_PASSWORD_REQUIRED)
	@Size(min = 8, message = ValidationMessages.PASSWORD_SIZE)
	private String newPassword;
}