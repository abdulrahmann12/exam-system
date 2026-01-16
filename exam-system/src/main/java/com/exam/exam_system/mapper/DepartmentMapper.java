package com.exam.exam_system.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.exam.exam_system.Entities.Department;
import com.exam.exam_system.dto.DepartmentCreateRequestDTO;
import com.exam.exam_system.dto.DepartmentGetResponseDTO;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

	
	@Mapping(target = "departmentId", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "college", ignore = true)
	Department toEntity(DepartmentCreateRequestDTO dto);
	
	@Mapping(target = "collegeId",source = "department.college.collegeId")
	@Mapping(target = "collegeName", source = "department.college.collegeName")
	DepartmentGetResponseDTO toDto(Department department);
}
