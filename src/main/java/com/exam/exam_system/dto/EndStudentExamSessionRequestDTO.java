package com.exam.exam_system.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EndStudentExamSessionRequestDTO {

    @NotNull(message = "Session id is required")
    private Long sessionId;
}
