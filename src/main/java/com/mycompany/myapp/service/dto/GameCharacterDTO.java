package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.GameCharacter} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GameCharacterDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    @Size(min = 2)
    private String name;

    @NotNull(message = "must not be null")
    private String email;

    @NotNull(message = "must not be null")
    @Min(value = 0)
    private Integer level;

    @NotNull(message = "must not be null")
    @Min(value = 0)
    private Integer experience;

    @NotNull(message = "must not be null")
    private String shape;

    @NotNull(message = "must not be null")
    private String color;

    private String accessory;

    @NotNull(message = "must not be null")
    private String programmingLanguage;

    @NotNull(message = "must not be null")
    private String uniqueLink;

    private ProgressDTO progress;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getAccessory() {
        return accessory;
    }

    public void setAccessory(String accessory) {
        this.accessory = accessory;
    }

    public String getProgrammingLanguage() {
        return programmingLanguage;
    }

    public void setProgrammingLanguage(String programmingLanguage) {
        this.programmingLanguage = programmingLanguage;
    }

    public String getUniqueLink() {
        return uniqueLink;
    }

    public void setUniqueLink(String uniqueLink) {
        this.uniqueLink = uniqueLink;
    }

    public ProgressDTO getProgress() {
        return progress;
    }

    public void setProgress(ProgressDTO progress) {
        this.progress = progress;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GameCharacterDTO)) {
            return false;
        }

        GameCharacterDTO gameCharacterDTO = (GameCharacterDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, gameCharacterDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GameCharacterDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", email='" + getEmail() + "'" +
            ", level=" + getLevel() +
            ", experience=" + getExperience() +
            ", shape='" + getShape() + "'" +
            ", color='" + getColor() + "'" +
            ", accessory='" + getAccessory() + "'" +
            ", programmingLanguage='" + getProgrammingLanguage() + "'" +
            ", uniqueLink='" + getUniqueLink() + "'" +
            ", progress=" + getProgress() +
            ", user=" + getUser() +
            "}";
    }
}
