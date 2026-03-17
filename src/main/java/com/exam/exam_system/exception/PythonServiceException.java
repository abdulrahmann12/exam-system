package com.exam.exam_system.exception;

public class PythonServiceException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public PythonServiceException(String message) {
        super(message);
    }

    public PythonServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
