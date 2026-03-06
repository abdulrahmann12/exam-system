package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class InvalidResetCodeException extends RuntimeException {
	private static final long serialVersionUID = 1L;

    public InvalidResetCodeException() {
        super(Messages.INVALID_RESET_CODE);
    }
}