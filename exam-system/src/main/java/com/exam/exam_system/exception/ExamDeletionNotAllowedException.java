package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class ExamDeletionNotAllowedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ExamDeletionNotAllowedException() {
        super(Messages.EXAM_DELETION_NOT_ALLOWED);
    }
}
