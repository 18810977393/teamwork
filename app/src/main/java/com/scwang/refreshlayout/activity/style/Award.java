package com.scwang.refreshlayout.activity.style;

import android.support.annotation.NonNull;

public class Award implements Comparable<Award>
{
    private String name;
    private int score,times;
    public Award(String name, int score, int times)
    {
        this.name =name;
        this.score = score;
        this.times = times;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public int getTimes() {
        return times;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public String toString()
    {
        String result = name + " " + score + " " + times;
        return result;
    }

    @Override
    public int compareTo(Award award) {
        if (getScore()>award.getScore())
            return 1;
        else
            {
                if (getScore()<award.getScore())
                    return -1;
                else
                    {
                        if (getName().compareTo(award.getName())>0)
                            return 1;
                        else
                            return 0;
                    }
    }}
}
