package com.exam.exam_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadQuestionsResponseDTO {

    private String message;
    private Integer totalQuestions;
}
