package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserProblemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserProblem.class);
        UserProblem userProblem1 = new UserProblem();
        userProblem1.setId(1L);
        UserProblem userProblem2 = new UserProblem();
        userProblem2.setId(userProblem1.getId());
        assertThat(userProblem1).isEqualTo(userProblem2);
        userProblem2.setId(2L);
        assertThat(userProblem1).isNotEqualTo(userProblem2);
        userProblem1.setId(null);
        assertThat(userProblem1).isNotEqualTo(userProblem2);
    }
}
