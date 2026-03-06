package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class SubjectDepartmentMismatchException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public SubjectDepartmentMismatchException() {
        super(Messages.SUBJECT_DEPARTMENT_MISMATCH);
    }
}
