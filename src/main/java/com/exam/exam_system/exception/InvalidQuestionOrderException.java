package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class InvalidQuestionOrderException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InvalidQuestionOrderException() {
        super(Messages.INVALID_QUESTION_ORDER);
    }
}
