package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class UserCollegeMismatchException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UserCollegeMismatchException() {
        super(Messages.USER_COLLEGE_MISMATCH);
    }
}