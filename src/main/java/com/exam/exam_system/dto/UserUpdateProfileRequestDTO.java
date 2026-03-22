package com.exam.exam_system.dto;

import com.exam.exam_system.config.ValidationMessages;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateProfileRequestDTO {

	@NotBlank(message = ValidationMessages.FIRST_NAME_REQUIRED)
	private String firstName;

	@NotBlank(message = ValidationMessages.LAST_NAME_REQUIRED)
	private String lastName;

	@Pattern(regexp = "^01[0-2,5]{1}[0-9]{8}$", message = ValidationMessages.PHONE_INVALID)
	private String phone;
}