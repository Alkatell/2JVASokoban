package com.supinfo.sokoban;

public class Launcher
{
    public static void main(String[] args)
    {
        play(1);
        System.out.println("end");
    }

    public static void play(int level)
    {
        System.out.println("play level " + level);
        Map map = new Map(level);
    }
}
