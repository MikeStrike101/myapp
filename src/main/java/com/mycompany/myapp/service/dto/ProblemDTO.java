package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Problem} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProblemDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    @Size(min = 5, max = 150)
    private String title;

    @NotNull(message = "must not be null")
    @Size(min = 20)
    private String description;

    @NotNull(message = "must not be null")
    @Min(value = 1)
    private Integer level;

    @NotNull(message = "must not be null")
    @Min(value = 0)
    private Integer xpReward;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getXpReward() {
        return xpReward;
    }

    public void setXpReward(Integer xpReward) {
        this.xpReward = xpReward;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProblemDTO)) {
            return false;
        }

        ProblemDTO problemDTO = (ProblemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, problemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProblemDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", level=" + getLevel() +
            ", xpReward=" + getXpReward() +
            "}";
    }
}
