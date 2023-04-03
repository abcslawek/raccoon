package com.slaweklida;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player {
    private int x;
    private int y;
    private int width;
    private int height;
    private final Color color = new Color(255, 0, 0);
    private int xVel;
    private int yVel;
    private Graphics2D g2D;
    private String direction = "";
    private int animationCount;
    private final int animationDelay = 1;
    final private int GRAVITY = 1;
    private int fallCount;
    private int jumpCount;
    private String spriteSheet = "idle.png";
    private int spriteIndex;
    private BufferedImage[] sprites;
    private BufferedImage sprite;
    private boolean isRunning = false;

    public Player(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

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
    }

    public void move_right(int vel){
        this.xVel = vel;
        if(!this.direction.equals("right")){
            this.direction = "right";
            this.animationCount = 0;
        }
    }

    public void loop(int fps){
        move(this.xVel, this.yVel);
        this.yVel += Math.min(2, (this.fallCount / fps) * this.GRAVITY);
        this.fallCount += 20;
        updateSprite();
    }

    public void updateSprite(){
        this.spriteSheet = "idle.png";
        if(this.xVel != 0 && this.isRunning)
            this.spriteSheet = "run.png";
        this.spriteIndex = (this.animationCount / this.animationDelay) % this.sprites.length;
        this.sprite = this.sprites[this.spriteIndex];
        this.animationCount += 1;
    }

    public void landed(){
        this.yVel = 0;
        this.fallCount = 0;
        this.jumpCount = 0;
    }

    //getters and setters
    public BufferedImage getSprite() {
        return sprite;
    }

    public void setSprite(BufferedImage sprite) {
        this.sprite = sprite;
    }

    public int getSpriteIndex() {
        return spriteIndex;
    }

    public void setSpriteIndex(int spriteIndex) {
        this.spriteIndex = spriteIndex;
    }

    public String getSpriteSheet() {
        return spriteSheet;
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

    public void setSprites(BufferedImage[] sprites) {
        this.sprites = sprites;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public void setSpriteSheet(String spriteSheet) {
        this.spriteSheet = spriteSheet;
    }

    public BufferedImage[] getSprites() {
        return sprites;
    }

    public int getAnimationCount() {
        return animationCount;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
