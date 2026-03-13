package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class SessionExpiredException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public SessionExpiredException() {
        super(Messages.TIME_EXCEEDED);
    }

    public SessionExpiredException(String message) {
        super(message);
    }
}
