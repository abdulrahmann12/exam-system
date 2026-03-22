package com.exam.exam_system.dto;

import com.exam.exam_system.config.ValidationMessages;

import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class DepartmentUpdateRequestDTO {

    @NotBlank(message = ValidationMessages.DEPARTMENT_NAME_REQUIRED)
    private String departmentName;
    
    @NotNull(message = ValidationMessages.COLLEGE_ID_REQUIRED)
    private Long collegeId;
}