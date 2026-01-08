package com.exam.exam_system.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuestionResultDTO {

    private String questionText;

    private String studentAnswer;
    private String correctAnswer;

    private Integer marks;
    private Integer obtainedMarks;
}
