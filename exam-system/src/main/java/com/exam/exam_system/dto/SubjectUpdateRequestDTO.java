package com.exam.exam_system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SubjectUpdateRequestDTO {

    @NotBlank(message = "Subject name is required")
    private String subjectName;

    @NotNull(message = "Department ID is required")
    private Long departmentId;

    @NotNull(message = "College ID is required")
    private Long collegeId;
    
    @NotBlank(message = "Subject Code is required")
	private String subjectCode;
}
