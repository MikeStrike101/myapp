package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.ExecutionCode;
import com.mycompany.myapp.service.dto.ExecutionCodeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ExecutionCode} and its DTO {@link ExecutionCodeDTO}.
 */
@Mapper(componentModel = "spring")
public interface ExecutionCodeMapper extends EntityMapper<ExecutionCodeDTO, ExecutionCode> {}
