package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class ExpiredResetCodeException extends RuntimeException {
	private static final long serialVersionUID = 1L;

    public ExpiredResetCodeException() {
        super(Messages.EXPIRED_RESET_CODE);
    }
}