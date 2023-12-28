package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.enumeration.Status;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Progress} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProgressDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private Status status;

    private Integer currentLesson;

    @NotNull(message = "must not be null")
    @Min(value = 0)
    private Integer xp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getCurrentLesson() {
        return currentLesson;
    }

    public void setCurrentLesson(Integer currentLesson) {
        this.currentLesson = currentLesson;
    }

    public Integer getXp() {
        return xp;
    }

    public void setXp(Integer xp) {
        this.xp = xp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProgressDTO)) {
            return false;
        }

        ProgressDTO progressDTO = (ProgressDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, progressDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProgressDTO{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", currentLesson=" + getCurrentLesson() +
            ", xp=" + getXp() +
            "}";
    }
}
