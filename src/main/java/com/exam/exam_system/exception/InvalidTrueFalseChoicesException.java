package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class InvalidTrueFalseChoicesException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InvalidTrueFalseChoicesException() {
        super(Messages.INVALID_TRUE_FALSE_CHOICES);
    }
}
