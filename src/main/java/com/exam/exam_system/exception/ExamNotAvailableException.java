package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class ExamNotAvailableException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ExamNotAvailableException() {
        super(Messages.EXAM_NOT_AVAILABLE);
    }

    public ExamNotAvailableException(String message) {
        super(message);
    }
}
