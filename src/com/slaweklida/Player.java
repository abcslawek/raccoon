package com.slaweklida;

import java.awt.*;

public class Player {
    private int x;
    private int y;
    private int width;
    private int height;
    private String heroName;
    private Color color = new Color(255, 0, 0);
    private int xVel;
    private int yVel;
    private Graphics2D g2D;
    private String direction = "";
    private int animationCount;
    final private int GRAVITY = 1;
    private int fallCount;


    public Player(int x, int y, int width, int height, String heroName) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.heroName = heroName;

        this.xVel = 0;
        this.yVel = 0;
        this.direction = "left";
        this.animationCount = 0;
        this.fallCount = 0;
    }

    public void move(int dx, int dy){
        this.x += dx;
        this.y += dy;
    }

    public void move_left(int vel){
        this.xVel = -1 * vel;
        if(!this.direction.equals("left")){
            this.direction = "left";
            this.animationCount = 0;
        }
        System.out.println("this.direction: " + this.direction);
    }

    public void move_right(int vel){
        this.xVel = vel;
        if(!this.direction.equals("right")){
            this.direction = "right";
            this.animationCount = 0;
        }
        System.out.println("this.direction: " + this.direction);
    }

    public void loop(int fps){
        this.yVel += Math.min(1, (this.fallCount / fps) * this.GRAVITY);
        move(this.xVel, this.yVel);

        this.fallCount += 1;
    }

    //getters and setters
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

    public Color getColor() {
        return color;
    }

    public String getDirection() {
        return direction;
    }

    public int getxVel() {
        return xVel;
    }

    public int getyVel() {
        return yVel;
    }

    public void setxVel(int xVel) {
        this.xVel = xVel;
    }

    public void setyVel(int yVel) {
        this.yVel = yVel;
    }
}