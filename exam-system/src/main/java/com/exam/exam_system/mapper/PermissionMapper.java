package com.exam.exam_system.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.exam.exam_system.Entities.Permission;
import com.exam.exam_system.dto.PermissionCreateRequestDTO;
import com.exam.exam_system.dto.PermissionGetResponseDTO;
import com.exam.exam_system.dto.PermissionUpdateRequestDTO;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    @Mapping(target = "permissionId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "active", ignore = true)
    Permission toEntity(PermissionCreateRequestDTO dto);

    @Mapping(target = "permissionId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Permission toEntity(PermissionUpdateRequestDTO dto);

    PermissionGetResponseDTO toDto(Permission permission);
}
