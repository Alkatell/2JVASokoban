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
        Player player = new Player();
        Map map = new Map(level, player);

        do
        {
            map.display();
            player.move(map);
        }
        while(!map.isDone());

        map.display();

        //new Window();
        //new Score();
    }
}
