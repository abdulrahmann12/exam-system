package com.exam.exam_system.dto;

import jakarta.validation.constraints.*;
import lombok.Data;


@Data
public class CreateChoiceRequestDTO {

    @NotBlank
    @Size(max = 1000)
    private String choiceText;

    @NotNull
    private Boolean isCorrect;

    @NotNull
    @Min(1)
    private Integer choiceOrder;
}

