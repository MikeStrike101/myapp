package com.mycompany.myapp.domain;

import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A ExecutionCode.
 */
@Table("execution_code")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExecutionCode implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("question_number")
    private Integer questionNumber;

    @Column("code")
    private String code;

    @NotNull(message = "must not be null")
    @Column("game_character")
    private Integer gameCharacter;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ExecutionCode id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuestionNumber() {
        return this.questionNumber;
    }

    public ExecutionCode questionNumber(Integer questionNumber) {
        this.setQuestionNumber(questionNumber);
        return this;
    }

    public void setQuestionNumber(Integer questionNumber) {
        this.questionNumber = questionNumber;
    }

    public String getCode() {
        return this.code;
    }

    public ExecutionCode code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getGameCharacter() {
        return this.gameCharacter;
    }

    public ExecutionCode gameCharacter(Integer gameCharacter) {
        this.setGameCharacter(gameCharacter);
        return this;
    }

    public void setGameCharacter(Integer gameCharacter) {
        this.gameCharacter = gameCharacter;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExecutionCode)) {
            return false;
        }
        return id != null && id.equals(((ExecutionCode) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExecutionCode{" +
            "id=" + getId() +
            ", questionNumber=" + getQuestionNumber() +
            ", code='" + getCode() + "'" +
            ", gameCharacter=" + getGameCharacter() +
            "}";
    }
}
