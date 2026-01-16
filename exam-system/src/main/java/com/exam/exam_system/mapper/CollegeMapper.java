package com.exam.exam_system.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.exam.exam_system.Entities.College;
import com.exam.exam_system.dto.CollegeCreateRequestDTO;
import com.exam.exam_system.dto.CollegeGetResponseDTO;

@Mapper(componentModel = "spring")
public interface CollegeMapper{

	
	@Mapping(target = "collegeId", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	College toEntity(CollegeCreateRequestDTO dto);
	
	CollegeGetResponseDTO toDto(College college);
}
