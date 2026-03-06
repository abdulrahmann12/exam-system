package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class InvalidMCQChoicesException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InvalidMCQChoicesException() {
        super(Messages.INVALID_MCQ_CHOICES);
    }
}
