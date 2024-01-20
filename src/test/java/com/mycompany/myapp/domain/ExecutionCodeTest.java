package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExecutionCodeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExecutionCode.class);
        ExecutionCode executionCode1 = new ExecutionCode();
        executionCode1.setId(1L);
        ExecutionCode executionCode2 = new ExecutionCode();
        executionCode2.setId(executionCode1.getId());
        assertThat(executionCode1).isEqualTo(executionCode2);
        executionCode2.setId(2L);
        assertThat(executionCode1).isNotEqualTo(executionCode2);
        executionCode1.setId(null);
        assertThat(executionCode1).isNotEqualTo(executionCode2);
    }
}
