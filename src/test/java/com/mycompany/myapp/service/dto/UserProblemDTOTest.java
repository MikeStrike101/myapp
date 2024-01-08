package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserProblemDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserProblemDTO.class);
        UserProblemDTO userProblemDTO1 = new UserProblemDTO();
        userProblemDTO1.setId(1L);
        UserProblemDTO userProblemDTO2 = new UserProblemDTO();
        assertThat(userProblemDTO1).isNotEqualTo(userProblemDTO2);
        userProblemDTO2.setId(userProblemDTO1.getId());
        assertThat(userProblemDTO1).isEqualTo(userProblemDTO2);
        userProblemDTO2.setId(2L);
        assertThat(userProblemDTO1).isNotEqualTo(userProblemDTO2);
        userProblemDTO1.setId(null);
        assertThat(userProblemDTO1).isNotEqualTo(userProblemDTO2);
    }
}
