package com.exam.exam_system.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class QuestionStudentViewDTO {

    private Long questionId;

    private String questionText;
    private String questionType; // MCQ / TRUE_FALSE / ESSAY
    private Integer marks;

    private List<ChoiceStudentViewDTO> choices;
}
