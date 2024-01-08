package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Problem;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.domain.UserProblem;
import com.mycompany.myapp.service.dto.ProblemDTO;
import com.mycompany.myapp.service.dto.UserDTO;
import com.mycompany.myapp.service.dto.UserProblemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserProblem} and its DTO {@link UserProblemDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserProblemMapper extends EntityMapper<UserProblemDTO, UserProblem> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    @Mapping(target = "problem", source = "problem", qualifiedByName = "problemId")
    UserProblemDTO toDto(UserProblem s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("problemId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProblemDTO toDtoProblemId(Problem problem);
}
