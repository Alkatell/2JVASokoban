package com.supinfo.sokoban;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

class ClavierListener implements KeyListener{
    private ArrayList<String> monMot = new ArrayList<String>();
    private String Word = "";
    private int direction = 0;

    public void keyTyped(KeyEvent event) {}
    public void keyReleased(KeyEvent event) {}
    public void keyPressed(KeyEvent event) {
        // les directions
        if(event.getKeyCode() == 37 || event.getKeyCode() == 38 || event.getKeyCode() == 39 || event.getKeyCode() == 40) {
            Select_Direction(event.getKeyCode());
        }
        // Copie de liste vers String
        for(int i=0; i<monMot.size(); i++) {
            this.Word += (this.monMot.get(i));
        }
        System.out.println(this.Word);
        this.Word = "";
    }


    public void Select_Direction (int Code) {
        switch (Code) {
            case 37:
                this.direction = 1; // gauche
                break;
            case 38:
                this.direction = 2; // haut
                break;
            case 39:
                this.direction = 3; // droite
                break;
            case 40:
                this.direction = 4; // bas
                break;
            default:
                this.direction = 0;
        }
    }

}
