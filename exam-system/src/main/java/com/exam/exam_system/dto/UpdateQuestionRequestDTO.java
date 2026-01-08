package com.exam.exam_system.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateQuestionRequestDTO {

    @NotNull(message = "Question id is required for update")
    private Long questionId;
    
    @NotBlank
    @Size(min = 5, max = 2000)
    private String questionText;

    @NotNull
    @Pattern(
        regexp = "MCQ|TRUE_FALSE|ESSAY",
        message = "Invalid question type"
    )
    private String questionType;

    @NotNull
    @Min(1)
    private Integer marks;

    private List<@Valid UpdateChoiceRequestDTO> choices;
}