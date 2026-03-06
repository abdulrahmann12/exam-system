package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class DuplicateQuestionOrderException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public DuplicateQuestionOrderException() {
        super(Messages.DUPLICATE_QUESTION_ORDER);
    }
}
