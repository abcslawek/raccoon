package com.slaweklida;

import javax.swing.*;
import java.awt.*;

public class Heart {
    private Image image;
    private boolean full;
    private int x;
    private int y;
    private int width;
    private int height;
    public Heart(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.full = true;
        this.image = new ImageIcon("src/com/slaweklida/imgs/heartImage.png").getImage();
    }

    public void looseHealth(){
        this.full = false;
        this.image = new ImageIcon("src/com/slaweklida/imgs/emptyHeartImage.png").getImage();
    }

    public void restoreHealth(){
        this.full = true;
        this.image = new ImageIcon("src/com/slaweklida/imgs/heartImage.png").getImage();
    }

    public Image getImage() {
        return image;
    }

    public boolean isFull() {
        return full;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
