package com.exam.exam_system.Entities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "departments")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Department {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long departmentId;
	
	private String departmentName;
	
	
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	@PrePersist
	public void setCreatedAt() {
		// TODO Auto-generated method stub
		createdAt = LocalDateTime.now();
		updatedAt = LocalDateTime.now();
	}

	@PreUpdate
	public void setUpdatedAt() {
		// TODO Auto-generated method stub
		updatedAt = LocalDateTime.now();
	}
}
