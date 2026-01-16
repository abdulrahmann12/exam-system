package com.exam.exam_system.dto;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class QuestionAdminViewDTO {

    private Long questionId;

    private String questionText;
    private String questionType; 
    private Integer marks;

    private Integer questionOrder; 
    private List<ChoiceAdminViewDTO> choices;

    private Long examId;        
    private String examTitle;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
