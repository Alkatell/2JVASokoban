package com.supinfo.sokoban;


//import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Window extends JFrame {
    public JTextField jtf;

    public Window() {
        this.setSize(0, 0); // taille de la fenetre
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // quand on ferme ca s'arrete
        this.setLocation(500000,0); //pour qu'elle apparaisse au milieu de l'ecran

        jtf = new JTextField();
        jtf.setPreferredSize(new Dimension(150, 30)); //dimension zone de texte
        JPanel top = new JPanel();
        //jtf.setBorder(null);
        jtf.addKeyListener(new ClavierListener());

        top.add(jtf);

        this.setContentPane(top);
        this.setVisible(true);
    }
}