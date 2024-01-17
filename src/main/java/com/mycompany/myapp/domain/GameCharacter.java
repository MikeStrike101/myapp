package com.mycompany.myapp.domain;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.OneToOne;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A GameCharacter.
 */
@Table("game_character")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GameCharacter implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Size(min = 2)
    @Column("name")
    private String name;

    @NotNull(message = "must not be null")
    @Column("email")
    private String email;

    @NotNull(message = "must not be null")
    @Min(value = 0)
    @Column("level")
    private Integer level;

    @NotNull(message = "must not be null")
    @Min(value = 0)
    @Column("experience")
    private Integer experience;

    @NotNull(message = "must not be null")
    @Column("shape")
    private String shape;

    @NotNull(message = "must not be null")
    @Column("color")
    private String color;

    @Column("accessory")
    private String accessory;

    @NotNull(message = "must not be null")
    @Column("programming_language")
    private String programmingLanguage;

    @NotNull(message = "must not be null")
    @Column("unique_link")
    private String uniqueLink;

    @Column("profile_picture")
    private String profilePicture;

    @Transient
    @OneToOne(mappedBy = "gameCharacter", cascade = CascadeType.ALL)
    private Progress progress;

    @Transient
    private User user;

    @Column("progress_id")
    private Long progressId;

    @Column("user_id")
    private String userId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public GameCharacter id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public GameCharacter name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public GameCharacter email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getLevel() {
        return this.level;
    }

    public GameCharacter level(Integer level) {
        this.setLevel(level);
        return this;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getExperience() {
        return this.experience;
    }

    public GameCharacter experience(Integer experience) {
        this.setExperience(experience);
        return this;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public String getShape() {
        return this.shape;
    }

    public GameCharacter shape(String shape) {
        this.setShape(shape);
        return this;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public String getColor() {
        return this.color;
    }

    public GameCharacter color(String color) {
        this.setColor(color);
        return this;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getAccessory() {
        return this.accessory;
    }

    public GameCharacter accessory(String accessory) {
        this.setAccessory(accessory);
        return this;
    }

    public void setAccessory(String accessory) {
        this.accessory = accessory;
    }

    public String getProgrammingLanguage() {
        return this.programmingLanguage;
    }

    public GameCharacter programmingLanguage(String programmingLanguage) {
        this.setProgrammingLanguage(programmingLanguage);
        return this;
    }

    public void setProgrammingLanguage(String programmingLanguage) {
        this.programmingLanguage = programmingLanguage;
    }

    public String getUniqueLink() {
        return this.uniqueLink;
    }

    public GameCharacter uniqueLink(String uniqueLink) {
        this.setUniqueLink(uniqueLink);
        return this;
    }

    public void setUniqueLink(String uniqueLink) {
        this.uniqueLink = uniqueLink;
    }

    public String getProfilePicture() {
        return this.profilePicture;
    }

    public GameCharacter profilePicture(String profilePicture) {
        this.setProfilePicture(profilePicture);
        return this;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Progress getProgress() {
        return this.progress;
    }

    public void setProgress(Progress progress) {
        this.progress = progress;
        this.progressId = progress != null ? progress.getId() : null;
    }

    public GameCharacter progress(Progress progress) {
        this.setProgress(progress);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
        this.userId = user != null ? user.getId() : null;
    }

    public GameCharacter user(User user) {
        this.setUser(user);
        return this;
    }

    public Long getProgressId() {
        return this.progressId;
    }

    public void setProgressId(Long progress) {
        this.progressId = progress;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String user) {
        this.userId = user;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GameCharacter)) {
            return false;
        }
        return id != null && id.equals(((GameCharacter) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GameCharacter{" +
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
            ", profilePicture='" + getProfilePicture() + "'" +
            "}";
    }
}
