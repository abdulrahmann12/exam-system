package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class ChoiceNotBelongToQuestionException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ChoiceNotBelongToQuestionException() {
        super(Messages.CHOICE_NOT_BELONG_TO_QUESTION);
    }
}
