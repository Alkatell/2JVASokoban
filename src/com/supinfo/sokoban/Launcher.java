package com.supinfo.sokoban;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

public class Launcher
{
    public static void main(String[] args)
    {
        // En fonction du nombre d'arguments, on effectue une action différente
        switch(args.length)
        {
            case 0:
                // Pas d'argument, on joue à partir du niveau 1
                Launcher.play(1);
                break;

            case 1:
                // Si on a un seul argument et qu'il vaut "--create", on lance le créateur de map
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
                // Si on a deux arguments, que le premier vaut "--level" ou "--score" et que le deuxième est un nombre
                if((args[0].equals("--level") || args[0].equals("--score")) && args[1].matches("\\d+"))
                {
                    int level = Integer.parseInt(args[1]);

                    // Si le niveau demandé existe, on joue ou on affiche les scores (en fonction du premier argument)
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

                    // Si le niveau demandé n'existe pas, on prévient l'utilisateur
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

            // Si on reçoit 3 arguments ou plus, on affiche les commandes qui existent pour aider l'utilisateur
            default:
                Launcher.help();
                break;
        }
    }

    /**
     * Lance le jeu à partir du niveau contenu dans la variable "level"
     * @param level
     */
    public static void play(int level)
    {
        // On lance le keyboard listener (jnativehook)
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

        // On instancie un joueur, et on lance le jeu
        Player player = new Player();
        GlobalScreen.getInstance().addNativeKeyListener(new KeyboardListener(player));
        player.play(level);
    }

    /**
     * Affiche les commandes supportées
     */
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
