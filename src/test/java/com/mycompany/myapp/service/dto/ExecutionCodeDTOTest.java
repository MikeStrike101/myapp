package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExecutionCodeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExecutionCodeDTO.class);
        ExecutionCodeDTO executionCodeDTO1 = new ExecutionCodeDTO();
        executionCodeDTO1.setId(1L);
        ExecutionCodeDTO executionCodeDTO2 = new ExecutionCodeDTO();
        assertThat(executionCodeDTO1).isNotEqualTo(executionCodeDTO2);
        executionCodeDTO2.setId(executionCodeDTO1.getId());
        assertThat(executionCodeDTO1).isEqualTo(executionCodeDTO2);
        executionCodeDTO2.setId(2L);
        assertThat(executionCodeDTO1).isNotEqualTo(executionCodeDTO2);
        executionCodeDTO1.setId(null);
        assertThat(executionCodeDTO1).isNotEqualTo(executionCodeDTO2);
    }
}
