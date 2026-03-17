package com.exam.exam_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParsedQuestionDTO {

    private String questionText;
    private String questionType;
    private Integer questionOrder;
    private Integer marks;
    private List<ParsedChoiceDTO> choices;
}
