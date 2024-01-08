package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A TestCase.
 */
@Table("test_case")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestCase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("input")
    private String input;

    @NotNull(message = "must not be null")
    @Column("output")
    private String output;

    @Transient
    @JsonIgnoreProperties(value = { "testCases" }, allowSetters = true)
    private Problem problem;

    @Column("problem_id")
    private Long problemId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TestCase id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInput() {
        return this.input;
    }

    public TestCase input(String input) {
        this.setInput(input);
        return this;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return this.output;
    }

    public TestCase output(String output) {
        this.setOutput(output);
        return this;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public Problem getProblem() {
        return this.problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
        this.problemId = problem != null ? problem.getId() : null;
    }

    public TestCase problem(Problem problem) {
        this.setProblem(problem);
        return this;
    }

    public Long getProblemId() {
        return this.problemId;
    }

    public void setProblemId(Long problem) {
        this.problemId = problem;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestCase)) {
            return false;
        }
        return id != null && id.equals(((TestCase) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestCase{" +
            "id=" + getId() +
            ", input='" + getInput() + "'" +
            ", output='" + getOutput() + "'" +
            "}";
    }
}
