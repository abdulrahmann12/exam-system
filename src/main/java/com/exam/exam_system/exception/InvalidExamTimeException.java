package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class InvalidExamTimeException extends RuntimeException {
	private static final long serialVersionUID = 1L;

    public InvalidExamTimeException() {
        super(Messages.INVALID_EXAM_TIME);
    }
}