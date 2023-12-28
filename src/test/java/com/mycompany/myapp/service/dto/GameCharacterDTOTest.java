package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GameCharacterDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GameCharacterDTO.class);
        GameCharacterDTO gameCharacterDTO1 = new GameCharacterDTO();
        gameCharacterDTO1.setId(1L);
        GameCharacterDTO gameCharacterDTO2 = new GameCharacterDTO();
        assertThat(gameCharacterDTO1).isNotEqualTo(gameCharacterDTO2);
        gameCharacterDTO2.setId(gameCharacterDTO1.getId());
        assertThat(gameCharacterDTO1).isEqualTo(gameCharacterDTO2);
        gameCharacterDTO2.setId(2L);
        assertThat(gameCharacterDTO1).isNotEqualTo(gameCharacterDTO2);
        gameCharacterDTO1.setId(null);
        assertThat(gameCharacterDTO1).isNotEqualTo(gameCharacterDTO2);
    }
}
