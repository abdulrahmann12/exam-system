package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class InvalidQuestionTypeException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InvalidQuestionTypeException() {
        super(Messages.INVALID_QUESTION_TYPE);
    }
}
