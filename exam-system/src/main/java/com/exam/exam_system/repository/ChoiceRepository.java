package com.exam.exam_system.repository;

import com.exam.exam_system.Entities.Choice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChoiceRepository extends JpaRepository<Choice, Long> {
    List<Choice> findByQuestion_QuestionId(Long questionId); 
}
