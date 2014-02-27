package com.supinfo.sokoban;

public class Cell
{
    public enum Type { PLAYER, WALL, BOX, TARGET, FREE };
    private Type type = Type.FREE;
    private boolean target = false;

    public Cell(Type type)
    {
        this.type = type;
    }

    public Type getType()
    {
        return this.type;
    }

    public void setTarget(boolean target)
    {
        this.target = target;
    }
}
