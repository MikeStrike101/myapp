package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A UserProblem.
 */
@Table("user_problem")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserProblem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("solved_at")
    private Instant solvedAt;

    @NotNull(message = "must not be null")
    @Min(value = 0)
    @Column("passed_test_cases")
    private Integer passedTestCases;

    @NotNull(message = "must not be null")
    @Min(value = 0)
    @Column("xp_awarded")
    private Integer xpAwarded;

    @Transient
    private User user;

    @Transient
    @JsonIgnoreProperties(value = { "testCases" }, allowSetters = true)
    private Problem problem;

    @Column("user_id")
    private String userId;

    @Column("problem_id")
    private Long problemId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserProblem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getSolvedAt() {
        return this.solvedAt;
    }

    public UserProblem solvedAt(Instant solvedAt) {
        this.setSolvedAt(solvedAt);
        return this;
    }

    public void setSolvedAt(Instant solvedAt) {
        this.solvedAt = solvedAt;
    }

    public Integer getPassedTestCases() {
        return this.passedTestCases;
    }

    public UserProblem passedTestCases(Integer passedTestCases) {
        this.setPassedTestCases(passedTestCases);
        return this;
    }

    public void setPassedTestCases(Integer passedTestCases) {
        this.passedTestCases = passedTestCases;
    }

    public Integer getXpAwarded() {
        return this.xpAwarded;
    }

    public UserProblem xpAwarded(Integer xpAwarded) {
        this.setXpAwarded(xpAwarded);
        return this;
    }

    public void setXpAwarded(Integer xpAwarded) {
        this.xpAwarded = xpAwarded;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
        this.userId = user != null ? user.getId() : null;
    }

    public UserProblem user(User user) {
        this.setUser(user);
        return this;
    }

    public Problem getProblem() {
        return this.problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
        this.problemId = problem != null ? problem.getId() : null;
    }

    public UserProblem problem(Problem problem) {
        this.setProblem(problem);
        return this;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String user) {
        this.userId = user;
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
        if (!(o instanceof UserProblem)) {
            return false;
        }
        return id != null && id.equals(((UserProblem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserProblem{" +
            "id=" + getId() +
            ", solvedAt='" + getSolvedAt() + "'" +
            ", passedTestCases=" + getPassedTestCases() +
            ", xpAwarded=" + getXpAwarded() +
            "}";
    }
}
