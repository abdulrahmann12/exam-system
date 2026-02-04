package com.exam.exam_system.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.exam.exam_system.Entities.Choice;
import com.exam.exam_system.dto.*;

@Mapper(componentModel = "spring")
public interface ChoiceMapper {

	@Mapping(target = "choiceId", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "question", ignore = true)
	Choice toEntity(CreateChoiceRequestDTO dto);
	
	@Mapping(target = "choiceId", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "question", ignore = true)
	Choice toEntity(UpdateChoiceRequestDTO dto);
	
	ChoiceStudentViewDTO toStudentViewDto(Choice choice);
	
	ChoiceAdminViewDTO toAdminViewDto(Choice choice);
}
