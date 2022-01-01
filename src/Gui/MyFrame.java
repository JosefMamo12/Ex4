package Gui;

import classes.Agent;
import classes.GameManger;
import classes.Pokemon;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 *Frame of the screen.
 */
public class MyFrame extends JFrame {


    public MyFrame(GameManger gm) {
        GraphDraw gd = new GraphDraw(gm);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.add(gd);
        this.setSize(1000, 800);
        this.setTitle("Graph");
//        this.setLocationRelativeTo(null);
        this.setResizable(true);
        this.setVisible(true);

    }

}