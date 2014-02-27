package com.supinfo.sokoban;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.lang.Exception;

public class Map
{
    private int level;
    private String path;
    private int width = 0;
    private int height = 0;
    private Cell cells[][];
    private ArrayList<Position> targets = new ArrayList<Position>();

    public Map(int level, Player player)
    {
        System.out.println("initialize level " + level);

        this.level = level;
        this.path = "maps/" + this.level + ".sok";
        this.detectSize();
        this.initialize(player);
    }

	public int getLastLevel(){
		File file = new File("maps");
		File[] files = file.listFiles();
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

        System.out.println("map size : " + this.width + ";" + this.height);
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
                            throw new Exception("Map " + this.path + " invalid : char '" + line[x] + "' found.");
                    }
                }

                y++;
            }

            scanner.close();
        }

        catch(FileNotFoundException e)
        {
            System.out.println(e.toString());
        }

        catch(Exception e)
        {
            System.out.println(e.getMessage());
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
}
