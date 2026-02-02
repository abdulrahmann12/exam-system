package com.exam.exam_system.exception;

import java.util.List;

import com.exam.exam_system.config.Messages;

public class PermissionNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PermissionNotFoundException() {
        super(Messages.PERMISSION_NOT_FOUND);
    }
    
    public PermissionNotFoundException(List<Long> permissionId) {
        super(Messages.PERMISSION_NOT_FOUND);
    }
}
