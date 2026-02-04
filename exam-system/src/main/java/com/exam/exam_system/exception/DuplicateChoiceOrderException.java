package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class DuplicateChoiceOrderException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public DuplicateChoiceOrderException() {
        super(Messages.DUPLICATE_CHOICE_ORDER);
    }
}
