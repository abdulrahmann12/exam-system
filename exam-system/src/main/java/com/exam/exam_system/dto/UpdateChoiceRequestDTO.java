package com.exam.exam_system.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UpdateChoiceRequestDTO {

    @NotNull(message = "Choice id is required for update")
    private Long choiceId;

    @Size(max = 1000)
    private String choiceText;

    private Boolean isCorrect;

    @Min(1)
    private Integer choiceOrder;
}
