package com.slaweklida;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MyPanel extends JPanel implements ActionListener {

    private Image backgroundImage;
    final int PANEL_WIDTH = 1920;
    final int PANEL_HEIGHT = 1080;
    private Timer timer;
    private Player player;
    private BufferedImage heroImage;
    private String heroName = "3_Dude_Monster";
    private String heroSheet = "idle.png";
    private ArrayList<BufferedImage[]> sprites;

    public MyPanel() {
        backgroundImage = new ImageIcon("src/com.slaweklida.imgs/backgroundGrey.png").getImage();
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));

        timer = new Timer(17, this);
        timer.start();

        player = new Player(100, 100, 32, 32, heroName);
    }

    public void paint(Graphics g) {
        //must have
        super.paint(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.scale(2, 2);

        //tło
        for (int i = 0; i < PANEL_WIDTH; i = i + 100) {
            for (int j = 0; j < PANEL_HEIGHT; j = j + 100) {
                g2D.drawImage(backgroundImage, i, j, null);
            }
        }

        //czerwony kwadrat
        g2D.setPaint(this.player.getColor());
        g2D.fillRect(this.player.getX(), this.player.getY(), this.player.getWidth(), this.player.getHeight());

        //bohater
        sprites = loadSpriteSheets("sprites", "3_Dude_Monster", 32, 32, this.player.getDirection());
        g2D.drawImage(sprites.get(this.player.getSpriteSheet())[this.player.currentIndexOfSprite(sprites.get(this.player.getSpriteSheet()))], this.player.getX(), 100, null);

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        this.player.loop(60);
        this.player.setxVel(0); //kwadrat nie porusza się ciągle w jedną stronę
        repaint(); //ciągłe rysowanie nowych klatek - musi być!
    }

    public BufferedImage[] flip(BufferedImage[] sprites) {
        for (int i = 0; i < sprites.length; i++) {
            BufferedImage sprite = sprites[i];
            AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
            tx.translate(-sprite.getWidth(null), 0);
            AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            sprites[i] = op.filter(sprite, null);
        }
        return sprites;
    }

    public BufferedImage flipSingleImage(BufferedImage sprite) {
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-sprite.getWidth(null), 0);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        sprite = op.filter(sprite, null);
        return sprite;
    }

    public ArrayList<BufferedImage[]> loadSpriteSheets(String dir1, String dir2, int width, int height, String direction) {
        BufferedImage image = null;
        BufferedImage[] subimages = new BufferedImage[0];
        ArrayList<BufferedImage[]> allSprites = new ArrayList<>();
        File folder = new File("src/com/slaweklida/imgs/" + dir1 + "/" + dir2);
        File[] files = folder.listFiles();
        for (File file : files) {
            try {
                image = ImageIO.read(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int amountOfSubimages = image.getWidth() / this.player.getWidth();
            subimages = new BufferedImage[amountOfSubimages];
            for (int i = 0; i < amountOfSubimages; i++) {
                subimages[i] = image.getSubimage(i * this.player.getWidth(), 0, this.player.getWidth(), this.player.getHeight());
            }
            if (direction.equals("left")) {
                subimages = flip(subimages);
            }
            allSprites.add(subimages);
        }
        return allSprites;
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

