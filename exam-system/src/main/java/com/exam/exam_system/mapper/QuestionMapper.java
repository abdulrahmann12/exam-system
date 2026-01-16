package com.exam.exam_system.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.exam.exam_system.Entities.Question;
import com.exam.exam_system.dto.*;

@Mapper(componentModel = "spring", uses = {ChoiceMapper.class, ExamMapper.class})
public interface QuestionMapper {

	@Mapping(target = "questionId", ignore = true)
	@Mapping(target = "exam", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "QuestionOrder", ignore = true)
	Question toEntity(CreateQuestionRequestDTO dto);
	
	@Mapping(source = "exam.examId", target = "examId")
	@Mapping(source = "exam.title", target = "examTitle")
	QuestionAdminViewDTO toAdminViewDto(Question question);

	QuestionStudentViewDTO toStudentViewDto(Question question);
}
