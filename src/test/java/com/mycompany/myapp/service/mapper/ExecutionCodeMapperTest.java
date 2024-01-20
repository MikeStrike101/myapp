package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExecutionCodeMapperTest {

    private ExecutionCodeMapper executionCodeMapper;

    @BeforeEach
    public void setUp() {
        executionCodeMapper = new ExecutionCodeMapperImpl();
    }
}
