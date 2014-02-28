package com.supinfo.sokoban;

import java.util.Date;

public class Score
{
    private long time_start;
    private long time_stop;

    public Score() {
        long ts = (new Date()).getTime();
        System.out.println(ts);
    }

    public void start() {
        long ts = (new Date()).getTime();
        this.time_start = ts;
    }

    public void stop() {
        long ts = (new Date()).getTime();
        this.time_stop = ts;
    }

    public long calcul() {
        return this.time_stop - this.time_start;
    }
}
