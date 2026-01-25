package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class InvalidVerificationCodeException extends RuntimeException {
	private static final long serialVersionUID = 1L;

    public InvalidVerificationCodeException() {
        super(Messages.INVALID_VERIFICATION_CODE);
    }
}