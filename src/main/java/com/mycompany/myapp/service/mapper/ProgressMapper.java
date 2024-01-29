package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Progress;
import com.mycompany.myapp.service.dto.ProgressDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Progress} and its DTO {@link ProgressDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProgressMapper extends EntityMapper<ProgressDTO, Progress> {
    @Mapping(target = "id", source = "id")
    Progress toEntity(ProgressDTO progressDTO);

    @Mapping(target = "id", source = "id")
    ProgressDTO toDto(Progress progress);
}
