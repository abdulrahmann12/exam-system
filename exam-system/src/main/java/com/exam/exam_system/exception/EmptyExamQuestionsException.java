package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class EmptyExamQuestionsException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public EmptyExamQuestionsException() {
        super(Messages.EMPTY_EXAM_QUESTIONS);
    }
}
