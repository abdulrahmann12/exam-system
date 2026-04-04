package com.exam.exam_system.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "students", indexes = {
    @Index(name = "idx_students_active", columnList = "is_active"),
    @Index(name = "idx_students_academic_year", columnList = "academic_year")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    private Long studentId;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, unique = true)
    private String studentCode;

    @Column(nullable = false)
    private Integer academicYear;
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    private LocalDateTime deactivatedAt;

    private LocalDateTime enrolledAt;

    @PrePersist
    public void onCreate() {
        this.enrolledAt = LocalDateTime.now();
    }
}