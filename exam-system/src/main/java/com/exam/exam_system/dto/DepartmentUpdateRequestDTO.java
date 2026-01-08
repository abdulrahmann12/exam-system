package com.exam.exam_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class DepartmentUpdateRequestDTO {

    @NotBlank(message = "Department name is required")
    private String departmentName;
}
