package com.exam.exam_system.dto;

import com.exam.exam_system.config.ValidationMessages;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateStudentExamSessionRequestDTO {

    @NotNull(message = ValidationMessages.EXAM_ID_REQUIRED)
    private Long examId;

    @NotBlank(message = ValidationMessages.SESSION_CODE_REQUIRED)
    private String sessionCode;
}