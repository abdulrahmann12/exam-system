package com.exam.exam_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.exam.exam_system.Entities.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

	List<Question> findByExamExamId(Long examId);
	
	@Query("""
		    select distinct q from Question q
		    left join fetch q.choices
		    where q.exam.id = :examId
		""")
		List<Question> findQuestionsWithChoices(@Param("examId") Long examId);

	
}
