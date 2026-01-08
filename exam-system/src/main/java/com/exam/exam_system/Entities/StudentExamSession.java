package com.exam.exam_system.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "student_exam_sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentExamSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    @Column(nullable = false, unique = true)
    private String sessionCode;

    @Column(nullable = false)
    private Boolean isActive;

    private LocalDateTime startedAt;
    private LocalDateTime endedAt;

    @OneToMany(
        mappedBy = "studentSession",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<StudentAnswer> answers;

    @PrePersist
    public void onCreate() {
        this.startedAt = LocalDateTime.now();
        this.isActive = true;
    }

    @PreUpdate
    public void onUpdate() {
        if (this.endedAt != null) {
            this.isActive = false;
        }
    }
}
