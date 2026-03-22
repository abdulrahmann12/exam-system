package com.exam.exam_system.dto;

import com.exam.exam_system.config.ValidationMessages;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserChangePasswordRequestDTO {

	@NotBlank(message = ValidationMessages.CURRENT_PASSWORD_REQUIRED)
	private String oldPassword;

	@NotBlank(message = ValidationMessages.NEW_PASSWORD_REQUIRED)
	@Size(min = 8, message = ValidationMessages.NEW_PASSWORD_SIZE)
	private String newPassword;
}