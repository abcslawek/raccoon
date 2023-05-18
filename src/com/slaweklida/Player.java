package com.slaweklida;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player {
    private int x;
    private int y;
    private int width;
    private int height;
    private int maskX;
    private int maskY;
    private int maskWidth;
    private int maskHeight;
    private final Color color = new Color(255, 0, 0);
    private int xVel;
    private int yVel;
    private String direction = "";
    private int animationCount;
    private int oneTimeAnimationCount;
    private final int animationDelay = 4; //7
    final private int GRAVITY = 1;
    final private static int VEL = 5;
    private int lifes;
    private int attackRange;
    private int fallCount;
    private int jumpCount;
    private String spriteSheet = "idle.png";
    private int spriteIndex;
    private BufferedImage[] sprites;
    private BufferedImage sprite;
    private boolean oneTimeAnimationPlaying = false;
    private boolean isAttacking = false;

    public Player(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.maskX = x; //x-5
        this.maskY = y; //y
        this.maskWidth = 32; //42
        this.maskHeight = 31; //31
        this.xVel = 0;
        this.yVel = 0;
        this.direction = "right";
        this.animationCount = 0;
        this.fallCount = 0;
        this.jumpCount = 0;
        this.lifes = 3;
        this.attackRange = 23;
    }

    public void move(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }

    public void updateMask() {
        this.maskX = this.x;
        this.maskY = this.y;
    }

    public void moveMask(int x, int y) {
        this.maskX += x;
        this.maskY += y;
    }

    public void moveLeft(int vel) {
        this.xVel = -1 * vel;
        if (!this.direction.equals("left")) {
            this.direction = "left";
            this.animationCount = 0;
        }
    }

    public void moveRight(int vel) {
        this.xVel = vel;
        if (!this.direction.equals("right")) {
            this.direction = "right";
            this.animationCount = 0;
        }
    }

    public void looseLife() {
        this.lifes -= 1;
    }

    public void loop(int fps) {
        move(this.xVel, this.yVel);
        updateMask();
        this.yVel += Math.min(1, (this.fallCount / fps) * this.GRAVITY);
        this.fallCount += 10; //20
        updateSprite();
    }

    public void playOneTimeAnimation(String animation) {
        this.oneTimeAnimationPlaying = true;
        this.oneTimeAnimationCount = 0;
        this.spriteSheet = animation;
    }

    public void updateSprite() {
        if (this.oneTimeAnimationPlaying) {
            if (this.oneTimeAnimationCount >= this.sprites.length * this.animationDelay) {
                this.oneTimeAnimationPlaying = false;
                if(this.spriteSheet.equals("attack.png")) this.isAttacking = false;
                if(this.spriteSheet.equals("death.png")) this.move(0, 1000); //po skończonej animacji teleportuje wroga w kosmos
            } else {
                this.spriteIndex = (this.oneTimeAnimationCount / (this.animationDelay)) % this.sprites.length;
                this.sprite = this.sprites[this.spriteIndex];
                this.oneTimeAnimationCount += 1;
            }
        } else {
            this.spriteSheet = "idle.png";
            if (this.xVel != 0)
                this.spriteSheet = "run.png";
            if (this.yVel > this.GRAVITY * 2)
                this.spriteSheet = "fall.png";
            if (this.yVel < this.GRAVITY * -2)
                this.spriteSheet = "jump.png";

            this.spriteIndex = (this.animationCount / this.animationDelay) % this.sprites.length;
            this.sprite = this.sprites[this.spriteIndex];
            this.animationCount += 1;
        }

    }

    public void attack(){
        playOneTimeAnimation("attack.png");
        this.isAttacking = true;
    }

    public void death(){
        playOneTimeAnimation("death.png");
    }

    public void landed() {
        this.yVel = 0;
        this.fallCount = 0;
        this.jumpCount = 0;
    }

    public void hitHead() {
        this.fallCount = 0;
        this.jumpCount++;
        this.yVel = -1 * this.yVel;
    }

    public void jump() {
        if (this.jumpCount <= 1) { //blokada na potrójne skakanie
            this.yVel = -this.GRAVITY * 8;
            this.animationCount = 0;
            this.jumpCount++;
            if (this.jumpCount == 1) {
                this.fallCount = 0;
            }
        }
    }


    //getters and setters


    public int getOneTimeAnimationCount() {
        return oneTimeAnimationCount;
    }

    public BufferedImage getSprite() {
        return sprite;
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

    public String getDirection() {
        return direction;
    }

    public int getxVel() {
        return xVel;
    }

    public int getyVel() {
        return yVel;
    }

    public static int getVEL() {
        return VEL;
    }

    public int getLifes() {
        return lifes;
    }

    public int getMaskX() {
        return maskX;
    }

    public int getMaskY() {
        return maskY;
    }

    public void setSprites(BufferedImage[] sprites) {
        this.sprites = sprites;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setXVel(int xVel) {
        this.xVel = xVel;
    }

    public int getMaskWidth() {
        return maskWidth;
    }

    public int getMaskHeight() {
        return maskHeight;
    }

    public int getAttackRange() {
        return attackRange;
    }

    public boolean isOneTimeAnimationPlaying() {
        return oneTimeAnimationPlaying;
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public void setAttacking(boolean attacking) {
        isAttacking = attacking;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
