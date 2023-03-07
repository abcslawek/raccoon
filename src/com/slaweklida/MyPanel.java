package com.slaweklida;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class MyPanel extends JPanel implements ActionListener {

    Image image;

    final int PANEL_WIDTH = 1920;
    final int PANEL_HEIGHT = 1080;
    Image enemy;
    Image backgroundImage;
    Timer timer;
    //int xVelocity = 1;
    //int yVelocity = 1;
    //int x = 0;
    //int y = 0;
    Player player;
    private BufferedImage heroImage;

    public MyPanel() {
        image = new ImageIcon("src/imgs/backgroundGrey.png").getImage();
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));

        //szopek
        //enemy = new ImageIcon("src/imgs/raccoon.png").getImage();

        //bohater
        try {
            heroImage = ImageIO.read(getClass().getResource("Dude_Monster_Idle_4.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        timer = new Timer(17, this);
        timer.start();

        player = new Player(100, 100, 50, 50);
    }

    public void paint(Graphics g){
        //must have
        super.paint(g);
        Graphics2D g2D = (Graphics2D) g;

        //tło
        for(int i = 0; i < PANEL_WIDTH; i = i + 100){
            for(int j = 0; j < PANEL_HEIGHT; j = j + 100){
                g2D.drawImage(image, i , j,null);
            }
        }

        //szopek
        //g2D.drawImage(this.enemy, this.x, this.y, null);

        //czerwony kwadrat
        g2D.setPaint(this.player.getColor());
        g2D.fillRect(this.player.getX(), this.player.getY(), this.player.getWidth(), this.player.getHeight());

        //bohater
        BufferedImage subimage = this.heroImage.getSubimage(0,0,32,32);
        g2D.drawImage(subimage, 0, 0, 32, 32, null);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.player.loop(60);
        this.player.setxVel(0); //kwadrat nie porusza się ciągle w jedną stronę
        repaint(); //ciągłe rysowanie nowych klatek - musi być!
    }

    //getter
    public Player getPlayer() {
        return this.player;
    }
}

//g2D.setPaint(Color.blue);
//g2D.setStroke(new BasicStroke(5));
//g2D.drawLine(0, 0, 500, 500);

//g2D.setPaint(Color.pink);
//g2D.drawRect(0, 0, 100, 200);
//g2D.fillRect(0, 0, 100, 200);

//g2D.setPaint(Color.orange);
//g2D.drawOval(0, 0, 100, 100);
//g2D.fillOval(0, 0, 100, 100);

//g2D.setPaint(Color.red);
//g2D.drawArc(0, 0, 100, 100, 0, 180);
//g2D.fillArc(0, 0, 100, 100, 0, 180);
//g2D.setPaint(Color.white);
//g2D.fillArc(0, 0, 100, 100, 180, 180);

//int[] xPoints = {150, 250, 350};
//int[] yPoints = {300, 150, 300};
//g2D.drawPolygon(xPoints, yPoints,3);
//g2D.fillPolygon(xPoints, yPoints, 3);

//g2D.setPaint(Color.magenta);
//g2D.setFont(new Font("Ink Free", Font.BOLD, 50));
//g2D.drawString("U R A WINNER! :D", 50, 50);

