package com.supinfo.sokoban;

public class Map
{
    private int level;
    private int width;
    private int height;
    private Cell cells[][];

    public Map(int level)
    {
        this.level = level;
        System.out.println("initialize level " + this.level);
    }
}
