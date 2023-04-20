package com.slaweklida;

public class EndBlock extends Block{
    private boolean reached = false;

    public EndBlock(int x, int y, int width, int height, String pattern) {
        super(x, y, width, height, pattern);
    }

    public boolean isReached() {
        return reached;
    }

    public void setReached(boolean reached) {
        this.reached = reached;
    }
}
