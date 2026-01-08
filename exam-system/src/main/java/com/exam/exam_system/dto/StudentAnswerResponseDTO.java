package com.exam.exam_system.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StudentAnswerResponseDTO {

    private Long answerId;
    private Long questionId;
    private Long choiceId;
    private String answerText;

    private Boolean isCorrect;
    private LocalDateTime answeredAt;
}
