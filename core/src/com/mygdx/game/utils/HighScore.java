package com.mygdx.game.utils;

import lombok.Getter;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;

@Getter
public class HighScore implements Serializable, Comparable<HighScore> {
    private int score;
    private Date date;

    public HighScore(int score){
        this.score = score;
        date = Calendar.getInstance().getTime();
    }

    @Override
    public int compareTo(HighScore o) {
        return score - o.score;
    }
}
