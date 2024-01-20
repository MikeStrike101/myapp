package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.ExecutionCode} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExecutionCodeDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private Integer questionNumber;

    @Lob
    private String code;

    @NotNull(message = "must not be null")
    private Integer gameCharacter;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(Integer questionNumber) {
        this.questionNumber = questionNumber;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getGameCharacter() {
        return gameCharacter;
    }

    public void setGameCharacter(Integer gameCharacter) {
        this.gameCharacter = gameCharacter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExecutionCodeDTO)) {
            return false;
        }

        ExecutionCodeDTO executionCodeDTO = (ExecutionCodeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, executionCodeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExecutionCodeDTO{" +
            "id=" + getId() +
            ", questionNumber=" + getQuestionNumber() +
            ", code='" + getCode() + "'" +
            ", gameCharacter=" + getGameCharacter() +
            "}";
    }
}
