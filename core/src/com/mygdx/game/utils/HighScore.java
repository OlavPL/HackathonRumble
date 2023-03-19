package com.mygdx.game.utils;

import lombok.Getter;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

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

    public static void serialize(ArrayList<HighScore> scores, int points){
        if(scores.size() >=5 ){
            scores.sort(new Comparator<HighScore>() {
                @Override
                public int compare(HighScore o1, HighScore o2) {
                    return o2.getScore() - o1.getScore();
                }
            });
            if(points > scores.get(scores.size()-1).getScore()) {
                scores.remove(scores.size() - 1);
                scores.add(new HighScore(points));
                scores.sort(new Comparator<HighScore>() {
                    @Override
                    public int compare(HighScore o1, HighScore o2) {
                        return o2.getScore() - o1.getScore();
                    }
                });
            }
        }
        else {
            scores.add(new HighScore(points));
            scores.sort(new Comparator<HighScore>() {
                @Override
                public int compare(HighScore o1, HighScore o2) {
                    return o2.getScore() - o1.getScore();
                }
            });
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Constants.SCORE_FILE_PATH))){
            oos.writeObject(scores);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<HighScore> deSerialize(){
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Constants.SCORE_FILE_PATH))){
            return (ArrayList<HighScore>) ois.readObject();
        } catch (IOException | ClassNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
        return new ArrayList<>();
    }
}

