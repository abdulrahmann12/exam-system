package com.exam.exam_system.dto;

import java.util.List;

import com.exam.exam_system.Entities.QuestionType;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateQuestionRequestDTO {

    @NotNull(message = "Question id is required for update")
    private Long questionId;
    
    @NotBlank
    @Size(min = 5, max = 2000)
    private String questionText;

    @NotNull(message = "Question type cannot be null")
    private QuestionType questionType;
    
    @NotNull(message = "Question order cannot be null")
    private Integer questionOrder;

    @NotNull
    @Min(1)
    private Integer marks;

    private List<@Valid UpdateChoiceRequestDTO> choices;
}