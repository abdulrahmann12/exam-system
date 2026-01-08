package com.exam.exam_system.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "exams")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long examId;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    /* ================= Relations ================= */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "college_id", nullable = false)
    private College college;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "createdBy", nullable = false)
    private User createdBy;

    /* ================= Exam Settings ================= */

    @Column(nullable = false)
    private Integer durationMinutes;

    @Column(nullable = false)
    private Integer perQuestionTimeSeconds;

    @Column(nullable = false)
    private Boolean allowBackNavigation;

    @Column(nullable = false)
    private Boolean randomizeQuestions;

    /* ================= Timing ================= */

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    /* ================= Questions ================= */

    @OneToMany(
        mappedBy = "exam",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Question> questions;

    /* ================= Auditing ================= */

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
