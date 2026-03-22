package com.exam.exam_system.dto;

import com.exam.exam_system.config.ValidationMessages;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeEmailRequestDTO {

	@Email(message = ValidationMessages.EMAIL_INVALID)
	@NotBlank(message = ValidationMessages.NEW_EMAIL_REQUIRED)
	private String newEmail;
}