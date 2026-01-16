package com.exam.exam_system.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.exam.exam_system.Entities.StudentAnswer;
import com.exam.exam_system.dto.CreateStudentAnswerRequestDTO;
import com.exam.exam_system.dto.StudentAnswerResponseDTO;

@Mapper(componentModel = "spring")
public interface StudentAnswerMapper {

	
    @Mapping(target = "answerId", ignore = true)
    @Mapping(target = "question", ignore = true)
    @Mapping(target = "studentSession", ignore = true)
    @Mapping(target = "choice", ignore = true)
    @Mapping(target = "isCorrect", ignore = true)
    @Mapping(target = "answeredAt", ignore = true)
    StudentAnswer toEntity(CreateStudentAnswerRequestDTO dto);

    
    @Mapping(target = "questionId", source = "question.questionId")
    @Mapping(target = "choiceId", source = "choice.choiceId")
    StudentAnswerResponseDTO toResponseDTO(StudentAnswer answer);
}
