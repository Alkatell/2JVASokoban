package com.supinfo.sokoban;

import org.jnativehook.GlobalScreen;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.lang.Exception;

public class Map
{
    private int level;
    private String path;
    private int width;
    private int height;
    private Cell cells[][];
    private ArrayList<Position> targets;
    public static final String PATH = "maps/";
    public static final String EXTENSION = ".sok";

    public Map(int level, Player player)
    {
        this.height = 0;
        this.width = 0;
        this.targets = new ArrayList<Position>();
        this.level = level;
        this.path = Map.PATH + this.level + Map.EXTENSION;
        this.detectSize();
        this.initialize(player);
    }

    /**
     * Renvoie le dernier niveau existant
     * @return int
     */
	public static int getLastLevel()
    {
        File file = new File(Map.PATH);
        File[] files = file.listFiles();

        if(files == null)
        {
            System.err.println("Maps introuvables.");
            System.exit(1);
        }

        return files.length;
    }

    /**
     * Détecte la taille de la map (nombre de lignes et de colonnes) afin de l'initialiser
     */
    private void detectSize()
    {
        try
        {
            Scanner scanner = new Scanner(new File(this.path));
            boolean isFirstLine = true;

            // On parcourt chaque ligne du fichier contenant la map afin d'obtenir le nombre total de lignes
            while(scanner.hasNextLine())
            {
                String line = scanner.nextLine();
                this.height++;

                // On compte le nombre de caractères de la première ligne (c'est le nombre total de colonnes)
                if(isFirstLine)
                {
                    this.width = line.length();
                    isFirstLine = false;
                }
            }

            scanner.close();
        }

        catch(FileNotFoundException e)
        {
            System.err.println(e.toString());
            GlobalScreen.unregisterNativeHook();
            System.exit(1);
        }
    }

    /**
     * Initialise la map : enregistre les positions des murs, de cases vides, des boites, des cibles et du joueur.
     * @param player Instance du joueur, afin d'initialiser sa position
     */
    private void initialize(Player player)
    {
        this.cells = new Cell[this.width][this.height];

        try
        {
            Scanner scanner = new Scanner(new File(this.path));
            int y = 0;

            // On parcourt chaque ligne du fichier contenant la map
            while(scanner.hasNextLine())
            {
                char[] line = scanner.nextLine().toCharArray();

                // Pour chaque ligne, on parcours les caractères
                for(int x = 0; x < line.length; x++)
                {
                    // En fonction du caractère, on initialise le tableau "cells"
                    switch(line[x])
                    {
                        case '=':
                            this.cells[x][y] = new Cell(Cell.Type.WALL);
                            break;

                        case 'O':
                            this.cells[x][y] = new Cell(Cell.Type.TARGET);
                            this.cells[x][y].setTarget(true);
                            this.targets.add(new Position(x, y));
                            break;

                        case 'B':
                            this.cells[x][y] = new Cell(Cell.Type.BOX);
                            break;

                        case 'X':
                            this.cells[x][y] = new Cell(Cell.Type.PLAYER);
                            player.setPosition(x, y);
                            break;

                        case '.':
                            this.cells[x][y] = new Cell(Cell.Type.FREE);
                            break;

                        case 'V':
                            this.cells[x][y] = new Cell(Cell.Type.BOX);
                            this.cells[x][y].setTarget(true);
                            this.targets.add(new Position(x, y));
                            break;

                        default:
                            throw new Exception(
                                "Map " + this.path + " invalide :"
                                + " caractere '" + line[x] + "'"
                                + " ligne " + (x + 1) + ", colonne " + (y + 1)
                            );
                    }
                }

                y++;
            }

            scanner.close();
        }

        catch(Exception e)
        {
            System.err.println(e.getMessage());
            GlobalScreen.unregisterNativeHook();
            System.exit(1);
        }
    }

    /**
     * Affiche la map
     */
    public void display()
    {
        System.out.println("Niveau " + this.level + " :");

        // On parcourt le tableau "cells"
        for(int y = 0; y < this.height; y++)
        {
            for(int x = 0; x < this.width; x++)
            {
                String result;

                // En fonction du type de cellule, on affiche le bon caractère
                switch(this.cells[x][y].getType())
                {
                    case WALL:
                        result = "=";
                        break;

                    case FREE:
                        result = " ";
                        break;

                    case BOX:
                        result = "B";
                        break;

                    case PLAYER:
                        result = "X";
                        break;

                    case TARGET:
                        result = "O";
                        break;

                    default:
                        result = " ";
                        break;
                }

                System.out.print(result);
            }

            System.out.println();
        }

        System.out.println();
    }

    /**
     * Renvoie une cellule en fonction de la position reçue
     * @param position position de la cellule que l'on souhaite récupérer
     * @return Cell
     */
    public Cell getCell(Position position)
    {
        return this.cells[position.getX()][position.getY()];
    }

    /**
     * Renvoie une cellule en fonction de la position reçue
     * @param x position x de la cellule que l'on souhaite récupérer
     * @param y position y de la cellule que l'on souhaite récupérer
     * @return Cell
     */
    public Cell getCell(int x, int y)
    {
        return this.cells[x][y];
    }

    /**
     * Vérifie si la map est terminée
     * @return boolean
     */
    public boolean isDone()
    {
        // On parcourt le tableau contenant les positions des cibles
        for(int i = 0; i < this.targets.size(); i++)
        {
            // Si une case qui est une cible n'est pas occupée par une boite, la map n'est pas terminée
            if(this.getCell(this.targets.get(i)).getType() != Cell.Type.BOX)
            {
                return false;
            }
        }

        // Si toutes les ciblessont occupée par une boite, alors la map est terminée
        return true;
    }

    /**
     * Revoie la largeur de la map
     * @return int
     */
    public int getWidth()
    {
        return this.width;
    }

    /**
     * Renvoie la hauteur de la map
     * @return int
     */
    public int getHeight()
    {
        return this.height;
    }

    /**
     * Renvoie le niveau de la map
     * @return int
     */
	public int getLevel()
	{
		return this.level;
	}

    /**
     * Créateur de map : permet à l'utilisateur de créer sa map
     */
    public static void create()
    {
        try
        {
            ArrayList<String> lines = new ArrayList<String>();
            Scanner sc = new Scanner(System.in);
            boolean scan = true;
            int totalLines = 0;

            // On explique le fonctionnement à l'utilisateur
            System.out.println(
                "\nDessinez votre map, ligne par ligne. Pour terminer, entrez une ligne contenant uniquement 'P'.\n"
                + "Attention : pour que votre map soit valide, toutes les lignes doivent faire la meme taille,\n"
                + "votre map doit contenir un seul joueur et autant de boites que de cibles.\n\n"
                + "Caracteres autorises :\nmur : =\t\tcase vide : .\tjoueur : X\ncible : O\tboite : B\tboite sur une cible : V\n"
            );

            while(scan)
            {
                System.out.print("ligne " + (totalLines + 1) + "\t: ");
                String line = sc.nextLine(), error = "";
                boolean displayError = false;

                // Si l'utilisateur entre un P, la map est terminée
                if(line.equals("P") || line.equals("p"))
                {
                    scan = false;
                }

                // Sinon si la nouvelle ligne ne fait pas la même taille que la précédente, l'utilisateur doit recommencer la ligne
                else if(totalLines > 0 && line.length() != lines.get(totalLines - 1).length())
                {
                    error = "Attention : toutes les lignes doivent contenir le meme nombre de caracteres.";
                    displayError = true;
                }

                // Sinon si la ligne contient un ou plusieurs caractère(s) invalide(s), l'utilisateur doit recommencer la ligne
                else if(!line.matches("[=.XOBV]*"))
                {
                    error = "Attention : vous avez insere un ou plusieurs caractere(s) invalide(s).";
                    displayError = true;
                }


                // Sinon la ligne est valide, on l'enregistre
                else
                {
                    lines.add(line);
                    totalLines++;
                }

                // Si il y a une erreur, on l'affiche
                if(displayError)
                {
                    System.out.println("\n" + error);
                    System.out.println("Veuillez recommencer :\n");

                    for(int i = 0; i < lines.size(); i++)
                    {
                        System.out.println("ligne " + (i + 1) + "\t: " + lines.get(i));
                    }
                }
            }

            // Vérification de la taille de la map
            if(lines.size() <= 1 || lines.get(0).length() <= 1)
            {

                System.out.println("\nVotre map est invalide : elle doit etre composee d'au minimum deux lignes et deux colonnes.");
                return;
            }

            // Vérification de la composition de la map : on compte le nombre de boites, de cibles et de joueurs
            int boxes = 0, targets = 0, players = 0;

            for(int i = 0; i < lines.size(); i++)
            {
                char[] line = lines.get(i).toCharArray();

                for(int j = 0; j < line.length; j++)
                {
                    switch(line[j])
                    {
                        case 'B':
                            boxes++;
                            break;

                        case 'O':
                            targets++;
                            break;

                        case 'X':
                            players++;
                            break;

                        default:
                            break;
                    }
                }
            }

            // Si il n'y a pas exactement 1 joueur, la map est invalide
            if(players != 1)
            {
                System.out.println("\nVotre map est invalide : elle doit contenir un joueur.");
                return;
            }

            // Si il n'y a pas autant de boites que de cibles, la map est invalide
            else if(boxes != targets)
            {
                System.out.println("\nVotre map est invalide : elle doit contenir le meme nombre de boites que de cibles.");
                System.out.println("Elle contient actuellement " + boxes + " boites et " + targets + " cibles.");
                return;
            }

            // Si la map n'a pas de cibles (qui est égal au nombre de boites), la map n'est pas valide
            else if(boxes == 0)
            {
                System.out.println("\nVotre map est invalide : elle doit contenir au moins une boite et une cible.");
                return;
            }

            // On créer le nouveau fichier
            String path = Map.PATH + (Map.getLastLevel() + 1) + Map.EXTENSION;
            File newMap = new File(path);
            newMap.createNewFile();

            BufferedWriter map = new BufferedWriter(new FileWriter(path, true));

            for(int i = 0; i < lines.size(); i++)
            {
                map.write(lines.get(i));

                if(i < lines.size() - 1)
                {
                    map.write("\n");
                }

                map.flush();
            }

            map.close();

            // On indique à l'utilisateur où la map a été enregistrée
            System.out.println("\nMap disponible a l'adresse " + path + " !");
            System.out.println("Pour y jouer : --level " + Map.getLastLevel());
        }

        catch(IOException e)
        {
            System.err.print(e.getMessage());
        }
    }

    /**
     * Vérifie si une position est valide en fonction de la taille de la map
     * @param x position x de la cellule que l'on souhaite récupérer
     * @param y position y de la cellule que l'on souhaite récupérer
     * @return boolean
     */
    public boolean checkPosition(int x, int y)
    {
        return x >= 0 && x < this.getWidth() && y >= 0 && y < this.getHeight();
    }
}
