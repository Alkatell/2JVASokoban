package com.supinfo.sokoban;

import java.util.Date;

public class Score
{
    private long startedAt;
    private long stoppedAt;
    private boolean timerOn = false;

    public void startTimer()
    {
        this.startedAt = new Date().getTime();
        this.timerOn = true;
    }

    public void stopTimer()
    {
        this.stoppedAt = new Date().getTime();
        this.timerOn = false;
    }

    public long getResult()
    {
        return this.stoppedAt - this.startedAt;
    }

    public boolean timerIsOn()
    {
        return this.timerOn;
    }
}
