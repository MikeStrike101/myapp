package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.TestCase} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestCaseDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String input;

    @NotNull(message = "must not be null")
    private String output;

    private ProblemDTO problem;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public ProblemDTO getProblem() {
        return problem;
    }

    public void setProblem(ProblemDTO problem) {
        this.problem = problem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestCaseDTO)) {
            return false;
        }

        TestCaseDTO testCaseDTO = (TestCaseDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, testCaseDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestCaseDTO{" +
            "id=" + getId() +
            ", input='" + getInput() + "'" +
            ", output='" + getOutput() + "'" +
            ", problem=" + getProblem() +
            "}";
    }
}
