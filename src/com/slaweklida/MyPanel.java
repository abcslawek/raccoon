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
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class MyPanel extends JPanel implements ActionListener {

    private Image backgroundImage;
    final int PANEL_WIDTH = 1920;
    final int PANEL_HEIGHT = 1080;
    private int offsetX;
    private Timer timer;
    private Player player;
    private ArrayList<Block> blocks;
    private ArrayList<Block> collidedBlocks;
    private HashMap<String, BufferedImage[]> sprites;

    public MyPanel() {
        this.backgroundImage = new ImageIcon("src/com/slaweklida/imgs/backgroundGrey.png").getImage();
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.timer = new Timer(17, this); //17
        this.timer.start();
        this.player = new Player(300, 100, 32, 32);
        this.blocks = new ArrayList<>();
        this.collidedBlocks = new ArrayList<>();

        //this.blocks.add(new Block(0, 200, 100, 100, "blockImage"));
        this.blocks.add(new Block(400, 200, 100, 100, "blockImage"));
        this.blocks.add(new Block(500, 100, 100, 100, "blockImage"));
        //this.blocks.add(new Block(100, 200, 100, 100, "blockImage"));
        //this.blocks.add(new Block(200, 200, 100, 100, "blockImage"));
        this.blocks.add(new Block(300, 200, 100, 100, "blockImage"));

        //this.blocks.add(new Block(0, 0, 100, 100, "blockImage"));
    }

    public void paint(Graphics g) {
        //must have
        super.paint(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.scale(2, 2);

        //tło
        for (int i = 0; i < PANEL_WIDTH; i = i + 100) {
            for (int j = 0; j < PANEL_HEIGHT; j = j + 100) {
                g2D.drawImage(this.backgroundImage, i, j, null);
            }
        }

        //bohater
        g2D.drawImage(this.player.getSprite(), this.player.getX() - this.offsetX, this.player.getY(), null);

        //bloki
        for(Block block : this.blocks){
            g2D.drawImage(block.getImage(), block.getX() - this.offsetX, block.getY(), null);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //loading sprites to player
        this.sprites = loadSpriteSheets("sprites", "Raccoon", 32, 32, this.player.getDirection());
        this.player.setSprites(this.sprites.get(this.player.getSpriteSheet()));

        handleVerticalCollision();
        handleHorizontalScrolling();
        handleRespawn(300, 100);

        this.player.loop(60);
        repaint(); //ciągłe rysowanie nowych klatek - musi być!

        //System.out.println(horizontalCollide(this.player, this.blocks, this.player.getxVel()));

    }

    public void handleRespawn(int x, int y){
        if(this.player.getY() >= 500){
            this.player.setX(x);
            this.player.setY(y);
        }
    }

    public boolean isColliding(Player player, Block block, boolean fully){
        if(fully)
            return (player.getX() <= (block.getX() + block.getWidth())) &&
                    ((player.getX() + player.getWidth()) >= block.getX()) &&
                    (player.getY() <= (block.getY() + block.getHeight())) &&
                    ((player.getY() + player.getHeight()) >= block.getY());
        else
            return (player.getX() <= (block.getX() + block.getWidth())) &&
                    ((player.getX() + player.getWidth()) >= block.getX());
    }

    public boolean horizontalCollide(Player player, ArrayList<Block> blocks, int vel){
        this.player.move(vel, this.player.getY());
        for(Block block : blocks){
            if(isColliding(player, block, false)){
                this.player.move(-vel, this.player.getY());
                return true;
            }else{
                this.player.move(-vel, this.player.getY());
                return false;
            }
        }
        return false;
    }

    public void handleVerticalCollision() {
        this.collidedBlocks.clear();
        for (Block block : this.blocks) {
            if (isColliding(this.player, block, true)) {
                if(this.player.getyVel() > 0) {
                    this.player.setY(block.getY() - this.player.getHeight()); //ustawia gracza nad klockiem
                    this.player.landed(); //zeruje prędkość yVel
                }
                if(this.player.getyVel() < 0){
                    this.player.setY(block.getY() + block.getHeight()); //ustawia gracza pod klockiem
                    this.player.hitHead(); //odwraca prędkość yVel
                }
                this.collidedBlocks.add(block); //dodaje ten blok do bloków kolizyjnych
            }
        }
    }

    public void handleHorizontalScrolling(){
        int rightPlayerCornersX = this.player.getX() + this.player.getWidth();
        if(rightPlayerCornersX - this.offsetX  >= 600 && this.player.getxVel() > 0 ||
                (this.player.getX() - this.offsetX <= 50 && this.player.getxVel() < 0)) {
            this.offsetX += this.player.getxVel();
        }
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

    public HashMap<String, BufferedImage[]> loadSpriteSheets(String dir1, String dir2, int width, int height, String direction) {
        BufferedImage image = null;
        BufferedImage[] subimages = new BufferedImage[0];
        HashMap<String, BufferedImage[]> allSprites = new HashMap<>();
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
            allSprites.put(file.getName(), subimages);
        }
        return allSprites;
    }


    //getter
    public Player getPlayer() {
        return this.player;
    }

    public ArrayList<Block> getBlocks() {
        return blocks;
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

//czerwony kwadrat
//g2D.setPaint(this.player.getColor());
//g2D.fillRect(this.player.getX(), this.player.getY(), this.player.getWidth(), this.player.getHeight());