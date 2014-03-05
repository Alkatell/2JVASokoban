package com.supinfo.sokoban;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

public class Launcher
{
    public static void main(String[] args)
    {
        switch(args.length)
        {
            case 0:
                Launcher.play(1);
                break;

            case 1:
                if(args[0].equals("--create"))
                {
                    Map.create();
                }

                else
                {
                    Launcher.help();
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
                            Launcher.play(level);
                        }

                        else
                        {
                            Score.display(level);
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
                    Launcher.help();
                }
                break;

            default:
                Launcher.help();
                break;
        }
    }

    public static void play(int level)
    {
        try
        {
            GlobalScreen.registerNativeHook();
        }

        catch(NativeHookException e)
        {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(e.getMessage());

            System.exit(1);
        }

        Player player = new Player();
        GlobalScreen.getInstance().addNativeKeyListener(new KeyboardListener(player));
        player.play(level);
    }

    public static void help()
    {
        System.out.println(
            "Options supportees :\n"
            + "--help : afficher l'aide\n"
            + "--level X : jouer au niveau X\n"
            + "--score X : afficher les scores du niveau X\n"
            + "--create : lancer l'editeur de niveau"
        );
    }
}
