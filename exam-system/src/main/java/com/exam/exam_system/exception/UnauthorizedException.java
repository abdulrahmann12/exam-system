package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class UnauthorizedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UnauthorizedException() {
        super(Messages.UNAUTHORIZED);
    }
}
