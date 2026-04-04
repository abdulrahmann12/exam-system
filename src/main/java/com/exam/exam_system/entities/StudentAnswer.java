package com.exam.exam_system.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "student_answers", indexes = {
    @Index(name = "idx_sa_session_question", columnList = "student_session_id, question_id", unique = true)
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long answerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_session_id", nullable = false)
    private StudentExamSession studentSession;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "choice_id")
    private Choice choice; 

    private String answerText; 

    private Boolean isCorrect;

    private LocalDateTime answeredAt;

    @PrePersist
    public void onCreate() {
        this.answeredAt = LocalDateTime.now();
    }
}
