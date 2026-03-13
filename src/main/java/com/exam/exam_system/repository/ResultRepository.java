package com.exam.exam_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exam.exam_system.entities.Result;

import java.util.List;
import java.util.Optional;

public interface ResultRepository extends JpaRepository<Result, Long> {

    Optional<Result> findByStudentSession_SessionId(Long sessionId);

    boolean existsByStudentSession_SessionId(Long sessionId);

    List<Result> findByStudentSession_SessionIdIn(List<Long> sessionIds);
}
