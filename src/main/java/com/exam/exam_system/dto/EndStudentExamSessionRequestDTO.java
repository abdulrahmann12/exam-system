package com.exam.exam_system.dto;

import com.exam.exam_system.config.ValidationMessages;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EndStudentExamSessionRequestDTO {

    @NotNull(message = ValidationMessages.SESSION_ID_REQUIRED)
    private Long sessionId;
}