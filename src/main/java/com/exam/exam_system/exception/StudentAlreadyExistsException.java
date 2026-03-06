package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class StudentAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public StudentAlreadyExistsException() {
        super(Messages.STUDENT_ALREADY_EXISTS);
    }
}