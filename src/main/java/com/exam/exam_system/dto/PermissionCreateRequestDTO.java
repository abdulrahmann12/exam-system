package com.exam.exam_system.dto;

import com.exam.exam_system.config.ValidationMessages;
import com.exam.exam_system.entities.PermissionActions;
import com.exam.exam_system.entities.PermissionModules;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class PermissionCreateRequestDTO {

	@NotNull(message = ValidationMessages.PERMISSION_MODULE_REQUIRED)
	private PermissionModules module;

	@NotNull(message = ValidationMessages.PERMISSION_ACTION_REQUIRED)
	private PermissionActions action;

	@Size(max = 255, message = ValidationMessages.PERMISSION_DESCRIPTION_SIZE)
	private String description;
}