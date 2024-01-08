package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserProblemMapperTest {

    private UserProblemMapper userProblemMapper;

    @BeforeEach
    public void setUp() {
        userProblemMapper = new UserProblemMapperImpl();
    }
}
