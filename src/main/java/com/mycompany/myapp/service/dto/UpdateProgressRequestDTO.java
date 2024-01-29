package com.mycompany.myapp.service.dto;

import javax.validation.constraints.NotNull;

public class UpdateProgressRequestDTO {

    @NotNull
    private Long gameCharacterId;

    @NotNull
    private Integer nextQuestionNumber;

    @NotNull
    private Integer newXP;

    @NotNull
    private Integer newLevel;

    // Getters and Setters

    public Long getGameCharacterId() {
        return gameCharacterId;
    }

    public void setGameCharacterId(Long gameCharacterId) {
        this.gameCharacterId = gameCharacterId;
    }

    public Integer getNextQuestionNumber() {
        return nextQuestionNumber;
    }

    public void setNextQuestionNumber(Integer nextQuestionNumber) {
        this.nextQuestionNumber = nextQuestionNumber;
    }

    public Integer getNewXP() {
        return newXP;
    }

    public void setNewXP(Integer newXP) {
        this.newXP = newXP;
    }

    public Integer getNewLevel() {
        return newLevel;
    }

    public void setNewLevel(Integer newLevel) {
        this.newLevel = newLevel;
    }
}
