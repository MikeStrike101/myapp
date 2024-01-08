package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Problem;
import com.mycompany.myapp.domain.TestCase;
import com.mycompany.myapp.service.dto.ProblemDTO;
import com.mycompany.myapp.service.dto.TestCaseDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TestCase} and its DTO {@link TestCaseDTO}.
 */
@Mapper(componentModel = "spring")
public interface TestCaseMapper extends EntityMapper<TestCaseDTO, TestCase> {
    @Mapping(target = "problem", source = "problem", qualifiedByName = "problemId")
    TestCaseDTO toDto(TestCase s);

    @Named("problemId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProblemDTO toDtoProblemId(Problem problem);
}
