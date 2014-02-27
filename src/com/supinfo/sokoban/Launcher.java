package com.supinfo.sokoban;

import java.io.IOException;

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

	        //EFFACER LA CONSOLE
        }
        while(!map.isDone());

        map.display();

	    System.out.println("Vous avez gagné");
	    System.out.println("Appuyez sur une toucher pour continuer");

	    try {
		    System.in.read();
	    } catch (IOException e) {
		    e.printStackTrace();
	    }

	    if (map.getLevel() < map.getLastLevel())
	    {
			play(map.getLevel() + 1);
	    }

	    //new Window();
        //new Score();
    }
}
