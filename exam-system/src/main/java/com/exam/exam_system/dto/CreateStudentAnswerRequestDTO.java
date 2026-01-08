package com.exam.exam_system.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateStudentAnswerRequestDTO {

    @NotNull(message = "Session id is required")
    private Long sessionId;

    @NotNull(message = "Question id is required")
    private Long questionId;

    private Long choiceId;

    private String answerText;
}
