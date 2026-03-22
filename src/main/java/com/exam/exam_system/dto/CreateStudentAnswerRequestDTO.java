package com.exam.exam_system.dto;

import com.exam.exam_system.config.ValidationMessages;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateStudentAnswerRequestDTO {

    @NotNull(message = ValidationMessages.SESSION_ID_REQUIRED)
    private Long sessionId;

    @NotNull(message = ValidationMessages.QUESTION_ID_REQUIRED)
    private Long questionId;

    private Long choiceId;

    private String answerText;
}