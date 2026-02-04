package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class UserDepartmentMismatchException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UserDepartmentMismatchException() {
        super(Messages.USER_DEPARTMENT_MISMATCH);
    }
}
