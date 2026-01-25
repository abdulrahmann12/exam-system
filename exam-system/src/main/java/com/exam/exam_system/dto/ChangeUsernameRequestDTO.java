package com.exam.exam_system.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeUsernameRequestDTO {

	@NotBlank(message = "New username must not be blank")
	private String newUsername;
}
