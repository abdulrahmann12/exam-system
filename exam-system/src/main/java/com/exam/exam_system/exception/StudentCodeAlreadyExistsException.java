package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class StudentCodeAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public StudentCodeAlreadyExistsException() {
        super(Messages.STUDENT_CODE_ALREADY_EXISTS);
    }
}