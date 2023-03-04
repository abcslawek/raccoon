package com.slaweklida;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;

public class MyFrame extends JFrame implements KeyListener {
    private MyPanel panel;

    public MyFrame(){
        panel = new MyPanel();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);

        this.addKeyListener(this);

        this.setVisible(true);

    }

    public MyPanel getPanel() {
        return panel;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        this.panel.getPlayer().setxVel(0);
        switch (e.getKeyChar()){
            case 'a': this.panel.getPlayer().move_left(10);
            case 'd': this.panel.getPlayer().move_right(10);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println("You released key char: " + e.getKeyChar() + " : " + e.getKeyCode());
    }
}
