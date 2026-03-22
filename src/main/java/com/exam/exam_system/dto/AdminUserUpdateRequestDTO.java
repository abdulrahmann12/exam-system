package com.exam.exam_system.dto;

import com.exam.exam_system.config.ValidationMessages;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminUserUpdateRequestDTO {

    @NotNull(message = ValidationMessages.ROLE_ID_REQUIRED)
    private Long roleId;

    @NotNull(message = ValidationMessages.COLLEGE_ID_REQUIRED)
    private Long collegeId;

    @NotNull(message = ValidationMessages.DEPARTMENT_ID_REQUIRED)
    private Long departmentId;
    
	private Boolean isActive;
}