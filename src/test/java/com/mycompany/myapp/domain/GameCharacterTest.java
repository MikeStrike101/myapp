package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GameCharacterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GameCharacter.class);
        GameCharacter gameCharacter1 = new GameCharacter();
        gameCharacter1.setId(1L);
        GameCharacter gameCharacter2 = new GameCharacter();
        gameCharacter2.setId(gameCharacter1.getId());
        assertThat(gameCharacter1).isEqualTo(gameCharacter2);
        gameCharacter2.setId(2L);
        assertThat(gameCharacter1).isNotEqualTo(gameCharacter2);
        gameCharacter1.setId(null);
        assertThat(gameCharacter1).isNotEqualTo(gameCharacter2);
    }
}
