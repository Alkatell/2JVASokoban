package com.supinfo.sokoban;

import org.jnativehook.keyboard.NativeKeyEvent;

public class Player
{
    private Position position;
    private Map map;
    private Score score = new Score();

    public void setPosition(int x, int y)
    {
        this.position = new Position(x, y);
    }

    public void move(int direction)
    {
        // On calcule les deux prochaines positions en fonction de la direction
        int nextX1 = this.position.getX(), nextX2 = nextX1;
        int nextY1 = this.position.getY(), nextY2 = nextY1;

        switch(direction)
        {
            case NativeKeyEvent.VK_UP:
                nextY1--;
                nextY2 -= 2;
                break;

            case NativeKeyEvent.VK_DOWN:
                nextY1++;
                nextY2 += 2;
                break;

            case NativeKeyEvent.VK_LEFT:
                nextX1--;
                nextX2 -= 2;
                break;

            case NativeKeyEvent.VK_RIGHT:
                nextX1++;
                nextX2 += 2;
                break;

            default:
                break;
        }

        // Si la prochaine case existe
        if(this.map.checkPosition(nextX1, nextY1))
        {
            Cell.Type nextType1 = this.map.getCell(nextX1, nextY1).getType();

            // Si la prochaine case est vide, on se déplace
            if(nextType1 == Cell.Type.FREE || nextType1 == Cell.Type.TARGET)
            {
                // On modifie le type de la case actuelle
                this.map.getCell(this.position).reset();

                // On déplace le joueur
                this.map.getCell(nextX1, nextY1).setType(Cell.Type.PLAYER);
                this.position = new Position(nextX1, nextY1);
            }

            // Sinon si la prochaine case est une boîte, on tente de déplacer la boîte
            else if(nextType1 == Cell.Type.BOX && this.map.checkPosition(nextX2, nextY2))
            {
                Cell.Type nextType2 = this.map.getCell(nextX2, nextY2).getType();

                // Si la case derrière la boîte est vide, on déplace la boîte
                if(nextType2 == Cell.Type.FREE || nextType2 == Cell.Type.TARGET)
                {
                    // On modifie le type de la case actuelle
                    this.map.getCell(this.position).reset();

                    // On déplace le joueur et la boîte
                    this.map.getCell(nextX1, nextY1).setType(Cell.Type.PLAYER);
                    this.map.getCell(nextX2, nextY2).setType(Cell.Type.BOX);
                    this.position = new Position(nextX1, nextY1);
                }
            }
        }

        // On affiche la map
        this.map.display();

        // On passe au niveau suivant si la map est terminée
        if(this.map.isDone())
        {
            this.score.stopTimer();

            if(this.map.getLevel() < Map.getLastLevel())
            {
                System.out.println("Vous avez terminé le niveau " + this.map.getLevel() + " !");
                System.out.println("Votre score est : " + this.score.getResult());
                System.out.println("Appuyez sur la touche n pour passer au niveau suivant.");
            }

            else
            {
                System.out.println("Vous avez terminé tous les niveaux !");
                System.out.println("Appuyez sur la touche ECHAP pour passer terminer.");
            }
        }
    }

    public void playNextLevel()
    {
        this.play(this.map.getLevel() + 1);
    }

    public void play(int level)
    {
        this.map = new Map(level, this);
        this.map.display();
        this.score.startTimer();
    }

    public void replay()
    {
        this.play(this.map.getLevel());
    }

    public Score getScore()
    {
        return score;
    }

    public Map getMap()
    {
        return this.map;
    }
}
