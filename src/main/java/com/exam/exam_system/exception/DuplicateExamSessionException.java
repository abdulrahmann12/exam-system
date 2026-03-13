package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class DuplicateExamSessionException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DuplicateExamSessionException() {
        super(Messages.EXAM_SESSION_ALREADY_EXISTS);
    }
}
