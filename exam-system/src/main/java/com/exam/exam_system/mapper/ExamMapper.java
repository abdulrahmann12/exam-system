package com.exam.exam_system.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.exam.exam_system.Entities.Exam;
import com.exam.exam_system.dto.*;

@Mapper(componentModel = "spring", uses = {QuestionMapper.class, ChoiceMapper.class})
public interface ExamMapper {

	
	@Mapping(target = "examId", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "college", ignore = true)
	@Mapping(target = "department", ignore = true)
	@Mapping(target = "subject", ignore = true)
	@Mapping(target = "totalQuestions", ignore = true)
	@Mapping(target = "isActive", ignore = true)
	Exam toEntity(CreateExamRequestDTO dto);
	
	
	@Mapping(source = "college.collegeId", target = "collegeId")
	@Mapping(source = "college.collegeName", target = "collegeName")
	@Mapping(source = "department.departmentId", target = "departmentId")
	@Mapping(source = "department.departmentName", target = "departmentName")
	@Mapping(source = "subject.subjectId", target = "subjectId")
	@Mapping(source = "subject.subjectName", target = "subjectName")
	@Mapping(source = "createdBy.userId", target = "createdById")
	@Mapping(source = "createdBy.username", target = "createdByName")
	ExamResponseDTO toDto(Exam exam);
	
	@Mapping(source = "subject.subjectName", target = "subjectName")
	ExamSummaryDTO toSummaryDto(Exam exam);
	
	
	ExamStudentViewDTO toStudentViewDto(Exam exam);
}
