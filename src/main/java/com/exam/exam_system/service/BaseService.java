package com.exam.exam_system.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class BaseService {

    protected Pageable createPageRequest(int page, int size, String sortBy) {
        return PageRequest.of(page, size, Sort.by(sortBy));
    }

    protected Pageable createPageRequest(int page, int size, String sortBy, Sort.Direction direction) {
        return PageRequest.of(page, size, Sort.by(direction, sortBy));
    }
}