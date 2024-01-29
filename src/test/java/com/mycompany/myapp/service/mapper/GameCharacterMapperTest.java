package com.mycompany.myapp.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class GameCharacterMapperTest {

    private GameCharacterMapper gameCharacterMapper;

    @BeforeEach
    public void setUp() {
        gameCharacterMapper = new GameCharacterMapperImpl();
    }
}
