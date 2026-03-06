package com.exam.exam_system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateStudentExamSessionRequestDTO {

    @NotNull(message = "Exam id is required")
    private Long examId;

    @NotBlank(message = "Session code is required")
    private String sessionCode;
}
