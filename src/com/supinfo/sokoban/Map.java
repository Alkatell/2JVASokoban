package com.supinfo.sokoban;

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

    private void detectSize()
    {
        try
        {
            Scanner scanner = new Scanner(new File(this.path));
            boolean isFirstLine = true;

            while(scanner.hasNextLine())
            {
                String line = scanner.nextLine();
                this.height++;

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
            System.out.println(e.toString());
        }
    }

    private void initialize(Player player)
    {
        this.cells = new Cell[this.width][this.height];

        try
        {
            Scanner scanner = new Scanner(new File(this.path));
            int y = 0;

            while(scanner.hasNextLine())
            {
                char[] line = scanner.nextLine().toCharArray();

                for(int x = 0; x < line.length; x++)
                {
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
                                + " caractère '" + line[x] + "'"
                                + " ligne " + (x + 1) + ", colonne " + (y + 1)
                            );
                    }
                }

                y++;
            }

            scanner.close();
        }

        catch(FileNotFoundException e)
        {
            System.err.println(e.toString());
            System.exit(1);
        }

        catch(Exception e)
        {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public void display()
    {
        System.out.println("Niveau " + this.level + " :");

        for(int y = 0; y < this.height; y++)
        {
            for(int x = 0; x < this.width; x++)
            {
                String result;

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
    }

    public Cell getCell(Position position)
    {
        return this.cells[position.getX()][position.getY()];
    }

    public Cell getCell(int x, int y)
    {
        return this.cells[x][y];
    }

    public boolean isDone()
    {
        for(int i = 0; i < this.targets.size(); i++)
        {
            if(this.getCell(this.targets.get(i)).getType() != Cell.Type.BOX)
            {
                return false;
            }
        }

        return true;
    }

    public int getWidth()
    {
        return this.width;
    }

    public int getHeight()
    {
        return this.height;
    }

	public int getLevel()
	{
		return this.level;
	}

    public static void create()
    {
        try
        {
            // On demande le nombre de lignes
            Scanner sc = new Scanner(System.in);
            int h;

            do
            {
                System.out.print("Combien de lignes votre map va-t-elle faire ? ");
                h = sc.nextInt();
            }
            while(h <= 0);

            // On demande à l'utilisateur d'entrer sa map
            String[] lines = new String[h];
            sc = new Scanner(System.in);

            System.out.println(
                "\nDessinez votre map, ligne par ligne.\n\n"
                + "Caractères autorisés :\nmur : =\t\tcase vide : .\tjoueur : X\ncible : O\tboite : B\tboite sur une cible : V\n"
            );

            for(int i = 0; i < h; i++)
            {
                System.out.print("ligne " + (i + 1) + "/" + h + ":\t");
                lines[i] = sc.nextLine();
            }

            // On créer le nouveau fichier
            String path = Map.PATH + (Map.getLastLevel() + 1) + Map.EXTENSION;
            File newMap = new File(path);
            newMap.createNewFile();

            BufferedWriter map = new BufferedWriter(new FileWriter(path, true));

            for(int i = 0; i < h; i++)
            {
                map.write(lines[i]);

                if(i < h - 1)
                {
                    map.write("\n");
                }

                map.flush();
            }

            map.close();

            System.out.println("\nMap créée à l'adresse " + path + " !");
        }

        catch(IOException e)
        {
            System.err.print(e.getMessage());
        }
    }

    public boolean checkPosition(int x, int y)
    {
        return x >= 0 && x < this.getWidth() && y >= 0 && y < this.getHeight();
    }
}
