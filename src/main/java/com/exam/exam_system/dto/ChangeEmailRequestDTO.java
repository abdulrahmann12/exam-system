package com.exam.exam_system.dto;

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

	@Email(message = "New email must be a valid email address")
	@NotBlank(message = "New email must not be blank")
	private String newEmail;
}
