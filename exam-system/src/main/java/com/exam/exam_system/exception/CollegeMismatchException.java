package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class CollegeMismatchException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public CollegeMismatchException() {
        super(Messages.COLLEGE_MISMATCH);
    }
}
