package com.exam.exam_system.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class BaseService {

    private static final int MAX_PAGE_SIZE = 100;

    protected Pageable createPageRequest(int page, int size, String sortBy) {
        int safeSize = Math.min(Math.max(size, 1), MAX_PAGE_SIZE);
        int safePage = Math.max(page, 0);
        return PageRequest.of(safePage, safeSize, Sort.by(sortBy));
    }

    protected Pageable createPageRequest(int page, int size, String sortBy, Sort.Direction direction) {
        int safeSize = Math.min(Math.max(size, 1), MAX_PAGE_SIZE);
        int safePage = Math.max(page, 0);
        return PageRequest.of(safePage, safeSize, Sort.by(direction, sortBy));
    }
}