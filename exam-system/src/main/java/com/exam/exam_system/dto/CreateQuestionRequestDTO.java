package com.exam.exam_system.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;


@Data
public class CreateQuestionRequestDTO {

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

    private List<@Valid CreateChoiceRequestDTO> choices;
}
