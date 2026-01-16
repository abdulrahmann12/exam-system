package com.exam.exam_system.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.exam.exam_system.Entities.Subject;
import com.exam.exam_system.dto.SubjectCreateRequestDTO;
import com.exam.exam_system.dto.SubjectGetResponseDTO;

@Mapper(componentModel = "spring")
public interface SubjectMapper {

	@Mapping(target = "subjectId", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "department", ignore = true)
	@Mapping(target = "college", ignore = true)
	Subject toEntity(SubjectCreateRequestDTO dto);
	
	@Mapping(target = "departmentId", source = "subject.department.departmentId")
	@Mapping(target = "departmentName", source = "subject.department.departmentName")
	@Mapping(target = "collegeId", source = "subject.college.collegeId")
	@Mapping(target = "collegeName", source = "subject.college.collegeName")
	SubjectGetResponseDTO toDto(Subject subject);
}
