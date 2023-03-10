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
        switch (e.getKeyChar()){
            case 'a':
                //System.out.println("You typed key char: " + e.getKeyChar() + " : " + e.getKeyCode());
                this.panel.getPlayer().move_left(5);
                this.panel.getPlayer().setRunning(true);
                break;
            case 'd':
                //System.out.println("You typed key char: " + e.getKeyChar() + " : " + e.getKeyCode());
                this.panel.getPlayer().move_right(5);
                this.panel.getPlayer().setRunning(true);
                break;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        //System.out.println("You released key char: " + e.getKeyChar() + " : " + e.getKeyCode());
        switch (e.getKeyChar()){
            case 'a':
                //System.out.println("You released key char: " + e.getKeyChar() + " : " + e.getKeyCode());
                this.panel.getPlayer().move_left(0);
                this.panel.getPlayer().setRunning(false);
                break;
            case 'd':
                //System.out.println("You released key char: " + e.getKeyChar() + " : " + e.getKeyCode());
                this.panel.getPlayer().move_right(0);
                this.panel.getPlayer().setRunning(false);
                break;
        }
    }
}
