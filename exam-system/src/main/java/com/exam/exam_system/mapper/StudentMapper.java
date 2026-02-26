package com.exam.exam_system.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.exam.exam_system.Entities.Student;
import com.exam.exam_system.dto.StudentCreateRequestDTO;
import com.exam.exam_system.dto.StudentGetResponseDTO;
import com.exam.exam_system.dto.StudentUpdateRequestDTO;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    @Mapping(target = "studentId", ignore = true)
    @Mapping(target = "user", ignore = true) 
    @Mapping(target = "enrolledAt", ignore = true)
    Student toEntity(StudentCreateRequestDTO dto);

    @Mapping(source = "user.firstName", target = "firstName")
    @Mapping(source = "user.lastName", target = "lastName")
    @Mapping(source = "user.email", target = "email")
    StudentGetResponseDTO toDto(Student student);

    @Mapping(target = "studentId", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "enrolledAt", ignore = true)
    void updateStudentFromDto(StudentUpdateRequestDTO dto, @MappingTarget Student student);
}