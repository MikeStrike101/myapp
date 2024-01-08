package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Problem.
 */
@Table("problem")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Problem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Size(min = 5, max = 150)
    @Column("title")
    private String title;

    @NotNull(message = "must not be null")
    @Size(min = 20)
    @Column("description")
    private String description;

    @NotNull(message = "must not be null")
    @Min(value = 1)
    @Column("level")
    private Integer level;

    @NotNull(message = "must not be null")
    @Min(value = 0)
    @Column("xp_reward")
    private Integer xpReward;

    @Transient
    @JsonIgnoreProperties(value = { "problem" }, allowSetters = true)
    private Set<TestCase> testCases = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Problem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Problem title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public Problem description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getLevel() {
        return this.level;
    }

    public Problem level(Integer level) {
        this.setLevel(level);
        return this;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getXpReward() {
        return this.xpReward;
    }

    public Problem xpReward(Integer xpReward) {
        this.setXpReward(xpReward);
        return this;
    }

    public void setXpReward(Integer xpReward) {
        this.xpReward = xpReward;
    }

    public Set<TestCase> getTestCases() {
        return this.testCases;
    }

    public void setTestCases(Set<TestCase> testCases) {
        if (this.testCases != null) {
            this.testCases.forEach(i -> i.setProblem(null));
        }
        if (testCases != null) {
            testCases.forEach(i -> i.setProblem(this));
        }
        this.testCases = testCases;
    }

    public Problem testCases(Set<TestCase> testCases) {
        this.setTestCases(testCases);
        return this;
    }

    public Problem addTestCases(TestCase testCase) {
        this.testCases.add(testCase);
        testCase.setProblem(this);
        return this;
    }

    public Problem removeTestCases(TestCase testCase) {
        this.testCases.remove(testCase);
        testCase.setProblem(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Problem)) {
            return false;
        }
        return id != null && id.equals(((Problem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Problem{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", level=" + getLevel() +
            ", xpReward=" + getXpReward() +
            "}";
    }
}
