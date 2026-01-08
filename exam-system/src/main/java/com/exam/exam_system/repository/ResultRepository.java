package com.exam.exam_system.repository;

import com.exam.exam_system.Entities.Result;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResultRepository extends JpaRepository<Result, Long> {

    Optional<Result> findByStudentSession_SessionId(Long sessionId);

    boolean existsByStudentSession_SessionId(Long sessionId);
}
