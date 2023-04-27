package com.slaweklida;

import java.awt.image.BufferedImage;
import java.util.HashMap;

public class FireBlock extends Block{
    private HashMap<String, BufferedImage[]> sprites;

    public FireBlock(int x, int y, int width, int height, String pattern) {
        super(x, y, width, height, pattern);

    }

}
