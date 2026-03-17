package com.exam.exam_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParsedExamDTO {

    private List<ParsedQuestionDTO> questions;
}
