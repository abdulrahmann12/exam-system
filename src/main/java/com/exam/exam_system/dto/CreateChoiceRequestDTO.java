package com.exam.exam_system.dto;

import com.exam.exam_system.config.ValidationMessages;

import jakarta.validation.constraints.*;
import lombok.Data;


@Data
public class CreateChoiceRequestDTO {

    @NotBlank(message = ValidationMessages.CHOICE_TEXT_REQUIRED)
    @Size(max = 1000)
    private String choiceText;

    @NotNull(message = ValidationMessages.CHOICE_IS_CORRECT_REQUIRED)
    private Boolean isCorrect;

    @NotNull(message = ValidationMessages.CHOICE_ORDER_REQUIRED)
    @Min(value = 1, message = ValidationMessages.CHOICE_ORDER_MIN)
    private Integer choiceOrder;
}