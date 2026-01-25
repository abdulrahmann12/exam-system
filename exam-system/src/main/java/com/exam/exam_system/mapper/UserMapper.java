package com.exam.exam_system.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.exam.exam_system.Entities.User;
import com.exam.exam_system.dto.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

	@Mapping(target = "roleName", source = "role.roleName")
	@Mapping(target = "collegeName", source = "college.collegeName")
	@Mapping(target = "departmentName", source = "department.departmentName")
	@Mapping(target = "username", expression = "java(user.getUsernameField())")
	UserResponseDTO toDTO(User user);

	@Mapping(target = "userId", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "isActive", ignore = true)
	@Mapping(target = "college", ignore = true)
	@Mapping(target = "department", ignore = true)
	@Mapping(target = "role", ignore = true)
	@Mapping(target = "password", source = "dto.password")
	@Mapping(target = "requestCode", ignore = true)
	@Mapping(target = "pendingEmail", ignore = true)
	@Mapping(target = "requestCodeExpiry", ignore = true)
	User toEntity(CreateUserRequestDTO dto);

	@Mapping(target = "roleName", source = "role.roleName")
	@Mapping(target = "collegeName", source = "college.collegeName")
	@Mapping(target = "departmentName", source = "department.departmentName")
	@Mapping(target = "username", expression = "java(user.getUsernameField())")

	UserSummaryDTO toSummaryDTO(User user);

	@Mapping(target = "roleName", source = "role.roleName")
	@Mapping(target = "collegeName", source = "college.collegeName")
	@Mapping(target = "departmentName", source = "department.departmentName")
	@Mapping(target = "username", expression = "java(user.getUsernameField())")

	UserProfileResponseDTO toUserProfileResponseDTO(User user);

}
