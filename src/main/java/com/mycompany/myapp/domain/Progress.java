package com.mycompany.myapp.domain;

import com.mycompany.myapp.domain.enumeration.Status;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Progress.
 */
@Table("progress")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Progress implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("status")
    private Status status;

    @Column("current_lesson")
    private Integer currentLesson;

    @NotNull(message = "must not be null")
    @Min(value = 0)
    @Column("xp")
    private Integer xp;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Progress id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Status getStatus() {
        return this.status;
    }

    public Progress status(Status status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getCurrentLesson() {
        return this.currentLesson;
    }

    public Progress currentLesson(Integer currentLesson) {
        this.setCurrentLesson(currentLesson);
        return this;
    }

    public void setCurrentLesson(Integer currentLesson) {
        this.currentLesson = currentLesson;
    }

    public Integer getXp() {
        return this.xp;
    }

    public Progress xp(Integer xp) {
        this.setXp(xp);
        return this;
    }

    public void setXp(Integer xp) {
        this.xp = xp;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Progress)) {
            return false;
        }
        return id != null && id.equals(((Progress) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Progress{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", currentLesson=" + getCurrentLesson() +
            ", xp=" + getXp() +
            "}";
    }
}
