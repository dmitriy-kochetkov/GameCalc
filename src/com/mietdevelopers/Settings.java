package com.mietdevelopers.GameCalc;

/**
 * Created by dmitriy on 16.03.16.
 */
public class Settings {

    private int playersCount;
    private int maxScore;

    public Settings(int playersCount, int maxScore) {
        this.playersCount = playersCount;
        this.maxScore = maxScore;
    }

    public Settings() {
        this.playersCount = 2;
        this.maxScore = 125;
    }

    public int getPlayersCount() {
        return playersCount;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void setPlayersCount(int playersCount) {
        if (playersCount >= 2)
            this.playersCount = playersCount;
        else
            this.playersCount = 2;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }
}
