package com.exam.exam_system.dto;

import com.exam.exam_system.config.ValidationMessages;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SubjectCreateRequestDTO {

    @NotBlank(message = ValidationMessages.SUBJECT_NAME_REQUIRED)
    private String subjectName;

    @NotNull(message = ValidationMessages.DEPARTMENT_ID_REQUIRED)
    private Long departmentId;

    @NotNull(message = ValidationMessages.COLLEGE_ID_REQUIRED)
    private Long collegeId;
    
    @NotBlank(message = ValidationMessages.SUBJECT_CODE_REQUIRED)
	private String subjectCode;
}