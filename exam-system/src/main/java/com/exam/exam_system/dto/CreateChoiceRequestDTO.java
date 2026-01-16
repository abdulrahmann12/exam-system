package com.exam.exam_system.dto;

import jakarta.validation.constraints.*;
import lombok.Data;


@Data
public class CreateChoiceRequestDTO {

    @NotBlank(message = "Choice text cannot be blank")
    @Size(max = 1000)
    private String choiceText;

    @NotNull(message = "isCorrect cannot be null")
    private Boolean isCorrect;

    @NotNull(message = "Choice order cannot be null")
    @Min(value = 1, message = "Choice order must be at least 1")
    private Integer choiceOrder;
}

