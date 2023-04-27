package com.slaweklida;

public class FlyingBlock extends Block{

    private int vel;
    private int range;
    private int beginningXPosition;
    private boolean flyingRight = true;

    public FlyingBlock(int x, int y, int width, int height, int vel, int range, String pattern) {
        super(x, y, width, height, pattern);
        this.vel = vel;
        this.range = range;
        this.beginningXPosition = x;
    }

    public void moveRight(){
        int newPosition = super.getX() + this.vel;
        super.setX(newPosition);
    }

    public void moveLeft(){
        int newPosition = super.getX() - this.vel;
        super.setX(newPosition);
    }

    public int getBeginningXPosition() {
        return beginningXPosition;
    }

    public int getRange() {
        return range;
    }

    public boolean isFlyingRight() {
        return flyingRight;
    }

    public void setFlyingRight(boolean flyingRight) {
        this.flyingRight = flyingRight;
    }

    public int getVel() {
        return vel;
    }
}
