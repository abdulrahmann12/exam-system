package com.exam.exam_system.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.exam.exam_system.Entities.StudentExamSession;
import com.exam.exam_system.dto.CreateStudentExamSessionRequestDTO;
import com.exam.exam_system.dto.StudentExamSessionResponseDTO;

@Mapper(componentModel = "spring",uses = {StudentAnswerMapper.class})
public interface StudentExamSessionMapper {

	@Mapping(target = "sessionId", ignore = true)
	@Mapping(target = "exam", ignore = true)
	@Mapping(target = "isActive", ignore = true)
	@Mapping(target = "startedAt", ignore = true)
	@Mapping(target = "endedAt", ignore = true)
	@Mapping(target = "answers", ignore = true)
	StudentExamSession toEntity(CreateStudentExamSessionRequestDTO dto);
	

    @Mapping(target = "examId", source = "exam.examId")
    StudentExamSessionResponseDTO toResponseDTO(StudentExamSession session);
}
