package com.exam.exam_system.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.exam.exam_system.Entities.Role;
import com.exam.exam_system.dto.RoleCreateRequestDTO;
import com.exam.exam_system.dto.RoleGetResponseDTO;

@Mapper(componentModel = "spring")
public interface RoleMapper {

	@Mapping(target = "roleId", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "permissions", ignore = true)
	Role toEntity(RoleCreateRequestDTO dto);
	
	RoleGetResponseDTO toDto(Role role);
}
