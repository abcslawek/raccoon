package com.slaweklida;

public class Main {

    public static void main(String[] args) {
	MyFrame myFrame = new MyFrame();
        while(true){
            myFrame.getPanel().getPlayer().loop(60);
        }

    }
}
