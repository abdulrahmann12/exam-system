package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class PermissionAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PermissionAlreadyExistsException() {
        super(Messages.PERMISSION_ALREADY_EXISTS);
    }
}
