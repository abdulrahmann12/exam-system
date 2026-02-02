package com.exam.exam_system.dto;

import com.exam.exam_system.Entities.PermissionActions;
import com.exam.exam_system.Entities.PermissionModules;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class PermissionCreateRequestDTO {

	@NotNull(message = "Module code is required")
	private PermissionModules module;

	@NotNull(message = "Action code is required")
	private PermissionActions action;

	@Size(max = 255, message = "Description must be less than 255 characters")
	private String description;
}
