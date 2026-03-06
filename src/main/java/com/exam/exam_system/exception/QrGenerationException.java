package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class QrGenerationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public QrGenerationException() {
        super(Messages.QR_GENERATION_FAILED); 
    }

    public QrGenerationException(String message) {
        super(message);
    }

    public QrGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
