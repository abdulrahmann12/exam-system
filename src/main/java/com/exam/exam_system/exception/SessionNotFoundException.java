package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class SessionNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public SessionNotFoundException() {
        super(Messages.EXAM_SESSION_NOT_FOUND);
    }
}
