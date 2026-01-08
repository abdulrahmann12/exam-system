package com.exam.exam_system.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChoiceStudentViewDTO {

    private Long choiceId;
    private String choiceText;
    private Integer choiceOrder;
}
