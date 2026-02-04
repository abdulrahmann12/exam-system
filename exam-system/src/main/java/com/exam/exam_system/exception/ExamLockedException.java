package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class ExamLockedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ExamLockedException() {
        super(Messages.EXAM_LOCKED);
    }
}
