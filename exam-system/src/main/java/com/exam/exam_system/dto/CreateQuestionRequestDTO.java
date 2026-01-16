package com.exam.exam_system.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;


@Data
public class CreateQuestionRequestDTO {

    @NotBlank(message = "Question text cannot be blank")
    @Size(min = 5, max = 2000)
    private String questionText;

    @NotNull
    @Pattern(
        regexp = "MCQ|TRUE_FALSE|ESSAY",
        message = "Invalid question type"
    )
    private String questionType;
    
    @NotNull(message = "Question order cannot be null")
    private Integer QuestionOrder;
    
    @NotNull(message = "Marks cannot be null")
    @Min(value = 1, message = "Marks must be at least 1")
    private Integer marks;

    private List<@Valid CreateChoiceRequestDTO> choices;
}
