package com.supinfo.sokoban;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Player
{
    private Position position;

    public Player()
    {

    }

    public void setPosition(int x, int y)
    {
        this.position = new Position(x, y);
    }

    public void move(Map map)
    {
        boolean validInput;

        do
        {
            int nextX1 = this.position.getX(), nextX2 = nextX1;
            int nextY1 = this.position.getY(), nextY2 = nextY1;
            validInput = true;

            System.out.print("\nMouvement : ");
            Scanner reader = new Scanner(System.in);

            switch(reader.next().charAt(0))
            {
                case 'z':
                case 'Z':
                    nextY1--;
                    nextY2 -= 2;
                    break;

                case 's':
                case 'S':
                    nextY1++;
                    nextY2 += 2;
                    break;

                case 'q':
                case 'Q':
                    nextX1--;
                    nextX2 -= 2;
                    break;

                case 'd':
                case 'D':
                    nextX1++;
                    nextX2 += 2;
                    break;

                default:
                    validInput = false;
                    break;
            }

            // Si input valide et position valide
            if(validInput && nextX1 >= 0 && nextX1 < map.getWidth() && nextY1 >= 0 && nextY1 < map.getHeight())
            {
                Cell.Type nextType1 = map.getCell(nextX1, nextY1).getType();
                Cell.Type nextType2 = map.getCell(nextX2, nextY2).getType();

                if(nextType1 == Cell.Type.FREE || nextType1 == Cell.Type.TARGET)
                {
                    if(map.getCell(this.position).isTarget())
                    {
                        map.getCell(this.position).setType(Cell.Type.TARGET);
                    }

                    else
                    {
                        map.getCell(this.position).setType(Cell.Type.FREE);
                    }

                    map.getCell(nextX1, nextY1).setType(Cell.Type.PLAYER);
                    this.setPosition(nextX1, nextY1);
                }

                else if(nextType1 == Cell.Type.BOX && (nextType2 == Cell.Type.FREE || nextType2 == Cell.Type.TARGET)
                        && nextX2 >= 0 && nextX2 < map.getWidth() && nextY2 >= 0 && nextY2 < map.getHeight())
                {
                    if(map.getCell(this.position).isTarget())
                    {
                        map.getCell(this.position).setType(Cell.Type.TARGET);
                    }

                    else
                    {
                        map.getCell(this.position).setType(Cell.Type.FREE);
                    }

                    map.getCell(nextX1, nextY1).setType(Cell.Type.PLAYER);
                    map.getCell(nextX2, nextY2).setType(Cell.Type.BOX);
                    this.setPosition(nextX1, nextY1);
                }
            }
        }
        while(!validInput);
    }
}
