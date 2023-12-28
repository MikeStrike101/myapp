package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Progress;
import com.mycompany.myapp.service.dto.ProgressDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Progress} and its DTO {@link ProgressDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProgressMapper extends EntityMapper<ProgressDTO, Progress> {}
