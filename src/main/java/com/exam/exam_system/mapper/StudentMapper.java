package com.exam.exam_system.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.exam.exam_system.dto.StudentCreateRequestDTO;
import com.exam.exam_system.dto.StudentGetResponseDTO;
import com.exam.exam_system.dto.StudentProfileResponseDTO;
import com.exam.exam_system.dto.StudentUpdateProfileRequestDTO;
import com.exam.exam_system.dto.StudentUpdateRequestDTO;
import com.exam.exam_system.dto.UserProfileResponseDTO;
import com.exam.exam_system.entities.Student;
import com.exam.exam_system.entities.User;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    @Mapping(target = "studentId", ignore = true)
    @Mapping(target = "user", ignore = true) 
    @Mapping(target = "enrolledAt", ignore = true)
    @Mapping(target = "deactivatedAt",ignore = true)
    @Mapping(target = "isActive",ignore = true)
    Student toEntity(StudentCreateRequestDTO dto);

    @Mapping(source = "user.firstName", target = "firstName")
    @Mapping(source = "user.lastName", target = "lastName")
    @Mapping(source = "user.email", target = "email")
    StudentGetResponseDTO toDto(Student student);

    @Mapping(target = "studentId", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "enrolledAt", ignore = true)
    @Mapping(target = "deactivatedAt",ignore = true)
    @Mapping(target = "isActive",ignore = true)
    void updateStudentFromDto(StudentUpdateRequestDTO dto, @MappingTarget Student student);

    @Mapping(target = "studentId", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "enrolledAt", ignore = true)
    @Mapping(target = "deactivatedAt", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    void updateStudentProfileFromDto(StudentUpdateProfileRequestDTO dto, @MappingTarget Student student);

   
    @Mapping(source = "studentId", target = "studentId")
    @Mapping(source = "studentCode", target = "studentCode")
    @Mapping(source = "academicYear", target = "academicYear")
    @Mapping(source = "isActive", target = "isActive")
    @Mapping(source = "user", target = "user", qualifiedByName = "toUserProfile")
    StudentProfileResponseDTO toProfileDto(Student student);

    @Named("toUserProfile")
    default UserProfileResponseDTO toUserProfile(User user) {
        if (user == null) return null;

        return UserProfileResponseDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .roleName(user.getRole() != null ? user.getRole().getRoleName() : null)
                .collegeName(user.getCollege() != null ? user.getCollege().getCollegeName() : null)
                .departmentName(user.getDepartment() != null ? user.getDepartment().getDepartmentName() : null)
                .createdAt(user.getCreatedAt())
                .build();
    }
}