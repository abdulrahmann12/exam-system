package com.exam.exam_system.dto;

import com.exam.exam_system.config.ValidationMessages;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UpdateChoiceRequestDTO {

    @NotNull(message = ValidationMessages.CHOICE_ID_REQUIRED)
    private Long choiceId;

    @Size(max = 1000)
    private String choiceText;

    private Boolean isCorrect;

    @Min(1)
    private Integer choiceOrder;
}