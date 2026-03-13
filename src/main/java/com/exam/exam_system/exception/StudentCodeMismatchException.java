package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class StudentCodeMismatchException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public StudentCodeMismatchException() {
        super(Messages.STUDENT_CODE_MISMATCH);
    }
}
