package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class ExamStartInPastException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ExamStartInPastException() {
        super(Messages.EXAM_START_IN_PAST);
    }
}
