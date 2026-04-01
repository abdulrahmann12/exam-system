package com.exam.exam_system.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PythonServiceErrorResponseDTO {

    private String errorCode;
    private String message;
    private List<String> details;
    private String path;
    private Date timestamp = new Date();

    public PythonServiceErrorResponseDTO(String errorCode, String message, List<String> details, String path) {
        this.errorCode = errorCode;
        this.message = message;
        this.details = details;
        this.path = path;
    }
}
