package com.exam.exam_system.exception;

import com.exam.exam_system.config.Messages;

public class PermissionDeletionNotAllowedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PermissionDeletionNotAllowedException() {
        super(Messages.CANNOT_DELETE_PERMISSION);
    }
}
// Compare this snippet from PermissionNotFoundException.java: