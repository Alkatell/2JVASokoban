package com.supinfo.sokoban;

import java.io.IOException;

public class Launcher
{
    public static void main(String[] args)
    {
        switch(args.length)
        {
            case 0:
                play(1);
                break;

            case 1:
                if(args[0].equals("--create"))
                {
                    Map.create();
                }

                else
                {
                    help();
                }
                break;

            case 2:
                if((args[0].equals("--level") || args[0].equals("--score")) && args[1].matches("\\d+"))
                {
                    int level = Integer.parseInt(args[1]);

                    if(level > 0 && level <= Map.getLastLevel())
                    {
                        if(args[0].equals("--level"))
                        {
                            play(level);
                        }

                        else
                        {
                            System.out.println("Afficher les scores du niveau " + level);
                        }
                    }

                    else
                    {
                        System.out.println(
                            "Le niveau " + level + " n'existe pas.\n"
                            + "Veuillez choisir un niveau entre 1 et " + Map.getLastLevel() + "."
                        );
                    }
                }

                else
                {
                    help();
                }
                break;

            default:
                break;
        }
    }

    public static void play(int level)
    {
        Player player = new Player();
        Map map;
        int nextAction;

        do
        {
            map = new Map(level, player);

            do
            {
                map.display();
                nextAction = player.move(map);
                // @todo Effacer la console
            }
            while(!map.isDone() && nextAction == Player.RESULT_CONTINUE);
        }
        while(nextAction == Player.RESULT_RESTART);

        if(nextAction == Player.RESULT_STOP)
        {
            return;
        }

        map.display();

	    System.out.println(
            "\nVous avez gagné le niveau " + map.getLevel() + ".\n"
            + "Appuyez sur la touche ENTREE pour continuer."
        );

	    try
        {
		    System.in.read();
	    }

        catch(IOException e)
        {
		    e.printStackTrace();
	    }

	    if(map.getLevel() < Map.getLastLevel())
	    {
			play(map.getLevel() + 1);
	    }

        else
        {
            System.out.println("Vous avez terminé tous les niveaux.");
        }

        //new Score();
    }

    public static void help()
    {
        System.out.println(
            "Options supportées :\n"
            + "--help : afficher l'aide\n"
            + "--level X : jouer au niveau X\n"
            + "--score X : afficher les scores du niveau X\n"
            + "--create : lancer l'éditeur de niveau"
        );
    }
}
