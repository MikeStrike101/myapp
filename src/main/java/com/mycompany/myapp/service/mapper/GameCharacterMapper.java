package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.GameCharacter;
import com.mycompany.myapp.domain.Progress;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.service.dto.GameCharacterDTO;
import com.mycompany.myapp.service.dto.ProgressDTO;
import com.mycompany.myapp.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link GameCharacter} and its DTO {@link GameCharacterDTO}.
 */
@Mapper(componentModel = "spring")
public interface GameCharacterMapper extends EntityMapper<GameCharacterDTO, GameCharacter> {
    @Mapping(target = "progress", source = "progress", qualifiedByName = "progressId")
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    GameCharacterDTO toDto(GameCharacter s);

    @Named("progressId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProgressDTO toDtoProgressId(Progress progress);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
