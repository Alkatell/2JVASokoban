package com.supinfo.sokoban;

public class Player
{
    private Position position;

    public Player(int x, int y)
    {
        this.position = new Position(x, y);
        System.out.println("player position : " + x + ";" + y);
    }
}