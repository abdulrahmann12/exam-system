package com.exam.exam_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class DepartmentUpdateRequestDTO {

    @NotBlank(message = "Department name is required")
    private String departmentName;
    
    @NotNull(message = "College is required")
    private Long collegeId;
}
