package com.exam.exam_system.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParsedChoiceDTO {

    private String choiceText;

    @JsonProperty("isCorrect")
    private Boolean isCorrect;

    private Integer choiceOrder;
}
