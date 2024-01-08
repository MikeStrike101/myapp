package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.UserProblem} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserProblemDTO implements Serializable {

    private Long id;

    private Instant solvedAt;

    @NotNull(message = "must not be null")
    @Min(value = 0)
    private Integer passedTestCases;

    @NotNull(message = "must not be null")
    @Min(value = 0)
    private Integer xpAwarded;

    private UserDTO user;

    private ProblemDTO problem;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getSolvedAt() {
        return solvedAt;
    }

    public void setSolvedAt(Instant solvedAt) {
        this.solvedAt = solvedAt;
    }

    public Integer getPassedTestCases() {
        return passedTestCases;
    }

    public void setPassedTestCases(Integer passedTestCases) {
        this.passedTestCases = passedTestCases;
    }

    public Integer getXpAwarded() {
        return xpAwarded;
    }

    public void setXpAwarded(Integer xpAwarded) {
        this.xpAwarded = xpAwarded;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
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
        if (!(o instanceof UserProblemDTO)) {
            return false;
        }

        UserProblemDTO userProblemDTO = (UserProblemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userProblemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserProblemDTO{" +
            "id=" + getId() +
            ", solvedAt='" + getSolvedAt() + "'" +
            ", passedTestCases=" + getPassedTestCases() +
            ", xpAwarded=" + getXpAwarded() +
            ", user=" + getUser() +
            ", problem=" + getProblem() +
            "}";
    }
}
