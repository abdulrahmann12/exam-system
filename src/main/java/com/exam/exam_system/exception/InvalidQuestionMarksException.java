package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class InvalidQuestionMarksException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InvalidQuestionMarksException() {
        super(Messages.INVALID_QUESTION_MARKS);
    }
}
