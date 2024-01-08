package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Problem;
import com.mycompany.myapp.service.dto.ProblemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Problem} and its DTO {@link ProblemDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProblemMapper extends EntityMapper<ProblemDTO, Problem> {}
