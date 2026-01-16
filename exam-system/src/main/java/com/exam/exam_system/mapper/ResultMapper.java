package com.exam.exam_system.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.exam.exam_system.Entities.Result;
import com.exam.exam_system.dto.ResultResponseDTO;

@Mapper(componentModel = "spring")
public interface ResultMapper {


    @Mapping(target = "sessionId", source = "studentSession.sessionId")
    ResultResponseDTO toResponseDTO(Result result);
}
