package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class QuestionNotBelongToExamException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public QuestionNotBelongToExamException() {
        super(Messages.QUESTION_NOT_BELONG_TO_EXAM);
    }
}
