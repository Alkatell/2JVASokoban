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
        Map map = new Map(level);

        do
        {
            map.display();
        }
        //while(!map.isDone());
        while(false);
        new Window();
        new Score();
    }
}
