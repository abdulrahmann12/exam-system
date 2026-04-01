package com.exam.exam_system.exception;

import java.util.List;

import lombok.Getter;

@Getter
public class PythonServiceException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final String errorCode;
    private final List<String> details;

    public PythonServiceException(String message) {
        super(message);
        this.errorCode = null;
        this.details = null;
    }

    public PythonServiceException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = null;
        this.details = null;
    }

    public PythonServiceException(String errorCode, String message, List<String> details) {
        super(message);
        this.errorCode = errorCode;
        this.details = details;
    }

    public PythonServiceException(String errorCode, String message, List<String> details, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.details = details;
    }
}
