package com.exam.exam_system.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "results")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resultId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_session_id", nullable = false, unique = true)
    private StudentExamSession studentSession;

    @Column(nullable = false)
    private Integer totalMark;

    @Column(nullable = false)
    private Integer maxMark;

    @Column(nullable = false)
    private LocalDateTime submittedAt;

    @PrePersist
    public void onCreate() {
        this.submittedAt = LocalDateTime.now();
    }
}
