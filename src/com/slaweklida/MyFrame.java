package com.slaweklida;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;

import static java.awt.event.KeyEvent.VK_SPACE;

public class MyFrame extends JFrame implements KeyListener {
    private MyPanel panel;

    public MyFrame() {
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
        switch (e.getKeyChar()) {
            case 'a':
                //System.out.println("You typed key char: " + e.getKeyChar() + " : " + e.getKeyCode());
                //if(!this.panel.horizontalCollide(this.panel.getPlayer(),
                //        this.panel.getBlocks(), this.panel.getPlayer().getxVel())) {
                    this.panel.getPlayer().move_left(Player.getVEL());
                    this.panel.getPlayer().setRunning(true);
                //}
                break;
            case 'd':
                //System.out.println("You typed key char: " + e.getKeyChar() + " : " + e.getKeyCode());
                //if(!this.panel.horizontalCollide(this.panel.getPlayer(),
                //        this.panel.getBlocks(), this.panel.getPlayer().getxVel())) {
                    this.panel.getPlayer().move_right(Player.getVEL());
                    this.panel.getPlayer().setRunning(true);
                //}
                break;
            case 32:
                this.panel.getPlayer().setY(this.panel.getPlayer().getY() - 1); // -1 usuwa buga i można skakać
                this.panel.getPlayer().jump();
                break;
        }
    }


    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        //System.out.println("You released key char: " + e.getKeyChar() + " : " + e.getKeyCode());
        switch (e.getKeyChar()) {
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
