package com.slaweklida;

public class Enemy extends Player{

    private int range;
    private int beginningXPosition;
    private boolean isMovingRight = true;

    public Enemy(int x, int y, int width, int height, int range) {
        super(x, y, width, height);
        this.range = range;
        this.beginningXPosition = x;
    }

    public int getRange() {
        return range;
    }

    public int getBeginningXPosition() {
        return beginningXPosition;
    }

    public boolean isMovingRight() {
        return isMovingRight;
    }

    public void setMovingRight(boolean movingRight) {
        isMovingRight = movingRight;
    }
}
