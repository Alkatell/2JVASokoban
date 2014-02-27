package com.supinfo.sokoban;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

class ClavierListener implements KeyListener{
    private ArrayList<String> monMot = new ArrayList<String>();
    private String Word = "";

    public void keyTyped(KeyEvent event) {}
    public void keyReleased(KeyEvent event) {}
    public void keyPressed(KeyEvent event) {
        // les directions
        if(event.getKeyCode() == 37 || event.getKeyCode() == 38 || event.getKeyCode() == 39 || event.getKeyCode() == 40) {
            System.out.println(Select_Direction(event.getKeyCode()));
        }
        // Copie de liste vers String
        for(int i=0; i<monMot.size(); i++) {
            this.Word += (this.monMot.get(i));
        }
        System.out.println(this.Word);
        this.Word = "";
    }


    public int Select_Direction (int Code) {
        switch (Code) {
            case 37:
                return 1; // gauche
            case 38:
                return 2; // haut
            case 39:
                return 3; // droite
            case 40:
                return 4; // bas
            default:
                return 0;
        }
    }

}
