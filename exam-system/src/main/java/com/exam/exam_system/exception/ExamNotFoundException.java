package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class ExamNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ExamNotFoundException() {
        super(Messages.EXAM_NOT_FOUND);
    }
}
