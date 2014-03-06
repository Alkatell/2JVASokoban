package com.supinfo.sokoban;

public class Cell
{
    public enum Type { PLAYER, WALL, BOX, TARGET, FREE }
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

    public void setType(Type type)
    {
        this.type = type;
    }

    public boolean isTarget()
    {
        return this.target;
    }

    public void setTarget(boolean target)
    {
        this.target = target;
    }

    /**
     * Permet de redéfinir le type d'une cellule (lorsqu'un joueur était sur une cible et qu'il se déplace, ou
     * lorsqu'une boite était sur une cible est que le joueur la déplace)
     */
    public void reset()
    {
        if(this.isTarget())
        {
            this.type = Cell.Type.TARGET;
        }

        else
        {
            this.type = Cell.Type.FREE;
        }
    }
}
