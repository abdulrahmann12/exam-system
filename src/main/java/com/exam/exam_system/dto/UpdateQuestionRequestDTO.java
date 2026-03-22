package com.exam.exam_system.dto;

import java.util.List;

import com.exam.exam_system.config.ValidationMessages;
import com.exam.exam_system.entities.QuestionType;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateQuestionRequestDTO {

    @NotNull(message = ValidationMessages.QUESTION_ID_REQUIRED)
    private Long questionId;
    
    @NotBlank(message = ValidationMessages.QUESTION_TEXT_REQUIRED)
    @Size(min = 5, max = 2000)
    private String questionText;

    @NotNull(message = ValidationMessages.QUESTION_TYPE_REQUIRED)
    private QuestionType questionType;
    
    @NotNull(message = ValidationMessages.QUESTION_ORDER_REQUIRED)
    private Integer questionOrder;

    @NotNull(message = ValidationMessages.QUESTION_MARKS_REQUIRED)
    @Min(value = 1, message = ValidationMessages.QUESTION_MARKS_MIN)
    private Integer marks;

    private List<@Valid UpdateChoiceRequestDTO> choices;
}