package com.supinfo.sokoban;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.lang.Exception;

public class Map
{
    private int level;
    private String path;
    private int width = 0;
    private int height = 0;
    private Cell cells[][];
    private Player player;

    public Map(int level)
    {
        System.out.println("initialize level " + level);

        this.level = level;
        this.path = "maps/" + this.level + ".sok";
        this.detectSize();
        this.initializeCells();
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

        System.out.println("map size : " + this.width + ";" + this.height);
    }

    private void initializeCells()
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

                        case 'o':
                            this.cells[x][y] = new Cell(Cell.Type.TARGET);
                            this.cells[x][y].setTarget(true);
                            break;

                        case 'B':
                            this.cells[x][y] = new Cell(Cell.Type.BOX);
                            break;

                        case 'X':
                            this.cells[x][y] = new Cell(Cell.Type.PLAYER);
                            this.player = new Player(x, y);
                            break;

                        case ' ':
                            this.cells[x][y] = new Cell(Cell.Type.FREE);
                            break;

                        default:
                            break;
                    }
                }

                y++;
            }

            scanner.close();
        }

        catch(FileNotFoundException e)
        {
            System.out.println("test1 " + e.toString());
        }
    }

    public void display()
    {
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
                        result = "o";
                        break;

                    default:
                        result = ".";
                        break;
                }

                System.out.print(result);
            }

            System.out.println();
        }
    }
}
