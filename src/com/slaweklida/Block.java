package com.slaweklida;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

abstract public class Block {
    private int x;
    private int y;
    private int width;
    private int height;
    private BufferedImage image = null;

    public Block(int x, int y, int width, int height, String pattern) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        //załadowanie obrazka na blok
        try {
            this.image = ImageIO.read(new File("src/com/slaweklida/imgs/" + pattern + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getImage() {
        return image;
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
