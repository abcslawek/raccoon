package com.slaweklida;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MyPanel extends JPanel implements ActionListener, KeyListener {

    private Image backgroundImage;
    final int PANEL_WIDTH = 1920;
    final int PANEL_HEIGHT = 1080;
    private int offsetX;
    private Timer timer;
    private Player player;
    private ArrayList<Block> blocks;
    private ArrayList<Block> collidedBlocks;
    private HashMap<String, BufferedImage[]> sprites;
    private boolean collideLeft = false;
    private boolean collideRight = false;

                                        private boolean aKeyPressed = false;
                                        private boolean dKeyPressed = false;
                                        private Timer miniTimer = new Timer(30, new ActionListener() {
                                            public void actionPerformed(ActionEvent e) {
                                                if (aKeyPressed) {
                                                    if (!collide(-Player.getVEL())) {
                                                        player.moveLeft(Player.getVEL()); //ustawiło xVel na -5
                                                        player.setRunning(true); //do zmiany sprita
                                                    }
                                                }
                                                if (dKeyPressed) {
                                                    if (!collide(Player.getVEL())) {
                                                        player.moveRight(Player.getVEL()); //ustawiło xVel na -5
                                                        player.setRunning(true); //do zmiany sprita
                                                    }
                                                }
                                            }
                                        });

    public MyPanel() {
        //zdarzenia z klawiatury
        this.addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        //reszta kodu
        this.backgroundImage = new ImageIcon("src/com/slaweklida/imgs/backgroundGrey.png").getImage();
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.timer = new Timer(17, this); //17
        this.timer.start();
        this.player = new Player(330, 100, 32, 32);
        this.blocks = new ArrayList<>();
        this.collidedBlocks = new ArrayList<>();

        //bloki
        this.blocks.add(new Block(400, 200, 100, 100, "blockImage"));
        this.blocks.add(new Block(500, 100, 100, 100, "blockImage"));
        this.blocks.add(new Block(300, 200, 100, 100, "blockImage"));
        this.blocks.add(new Block(200, 200, 100, 100, "blockImage"));
        this.blocks.add(new Block(100, 100, 100, 100, "blockImage"));
        this.blocks.add(new Block(200, 000, 100, 100, "blockImage"));
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

        //maska bohatera
        g2D.setPaint(Color.white);
        g2D.drawRect(this.player.getMaskX() - this.offsetX, this.player.getMaskY(),
                this.player.getMaskWidth(), this.player.getMaskHeight());

        //bohater
        g2D.drawImage(this.player.getSprite(), this.player.getX() - this.offsetX, this.player.getY(), null);

        //bloki
        for (Block block : this.blocks) {
            g2D.drawImage(block.getImage(), block.getX() - this.offsetX, block.getY(), null);
        }

        //parametry
        g2D.setPaint(new Color(153, 0, 255));
        g2D.setFont(new Font("Calibri", Font.BOLD, 13));
        g2D.drawString("X: " + this.player.getX(), 10, 50);
        g2D.drawString("Y: " + this.player.getY(), 10, 60);
        g2D.drawString("Mask X: " + this.player.getMaskX(), 10, 70);
        g2D.drawString("Mask Y: " + this.player.getMaskY(), 10, 80);
        g2D.drawString("xVel: " + this.player.getxVel(), 10, 90);
        g2D.drawString("yVel: " + this.player.getyVel(), 10, 100);
        g2D.drawString("Collided Blocks: " + this.collidedBlocks.size(), 10, 110);
        g2D.drawString("Mask colliding left: " + this.collideLeft, 10, 120);
        g2D.drawString("Mask colliding right: " + this.collideRight, 10, 130);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //loading sprites to player
        this.sprites = loadSpriteSheets("sprites", "Raccoon", 32, 32, this.player.getDirection());
        this.player.setSprites(this.sprites.get(this.player.getSpriteSheet()));

        handleVerticalCollision();
        handleHorizontalScrolling();
        handleRespawn(330, 100);

        this.player.loop(60); //jeśli isRunning to może się wykonać tylko raz
        repaint(); //narysowanie nowej klatki
    }

    public boolean collide(int vel) {
        this.player.moveMask(vel, 0);
        System.out.println("pre mX: " + this.player.getMaskX() + ", X: " + this.player.getX());
        for (Block block : this.blocks) {
            if (isMaskColliding(this.player, block)) {
                this.player.moveMask(-vel, 0);
                System.out.println("KOLIZJA - maska wróciła, mX: " + this.player.getMaskX() + ", X: " + this.player.getX());
                this.player.setxVel(0); //po respawnie gdy się idzie w kierunku ściany to postać się zatrzymuje
                return true;
            }
        }
        this.player.moveMask(-vel, 0);
        System.out.println("BRAK KOLIZJI - maska wróciła, mX: " + this.player.getMaskX() + ", X: " + this.player.getX());
        return false;
    }

    public boolean isPlayerColliding(Player player, Block block) {
        int x, y;
        x = player.getX();
        y = player.getY();
        return (x <= (block.getX() + block.getWidth())) &&
                ((x + player.getWidth()) >= block.getX()) &&
                (y <= (block.getY() + block.getHeight())) &&
                ((y + player.getHeight()) >= block.getY());
    }

    public boolean isMaskColliding(Player player, Block block) {
        int x, y;
        x = player.getMaskX();
        y = player.getMaskY();
        return (x <= (block.getX() + block.getWidth())) &&
                ((x + player.getMaskWidth()) >= block.getX()) &&
                (y <= (block.getY() + block.getHeight())) &&
                ((y + player.getMaskHeight()) >= block.getY());
    }

    public void handleVerticalCollision() {
        this.collidedBlocks.clear();
        for (Block block : this.blocks) {
            if (isPlayerColliding(this.player, block)) {
                if (this.player.getyVel() > 0) {
                    this.player.setY(block.getY() - this.player.getHeight()); //ustawia gracza nad klockiem
                    this.player.landed(); //zeruje prędkość yVel
                }
                if (this.player.getyVel() < 0) {
                    this.player.setY(block.getY() + block.getHeight()); //ustawia gracza pod klockiem
                    this.player.hitHead(); //odwraca prędkość yVel
                }
                this.collidedBlocks.add(block); //dodaje ten blok do bloków kolizyjnych
            }
        }
    }

    public void handleHorizontalScrolling() {
        int rightPlayerCornersX = this.player.getX() + this.player.getWidth();
        if (rightPlayerCornersX - this.offsetX >= 600 && this.player.getxVel() > 0 ||
                (this.player.getX() - this.offsetX <= 50 && this.player.getxVel() < 0)) {
            this.offsetX += this.player.getxVel();
        }
    }

    public void handleRespawn(int x, int y) {
        if (this.player.getY() >= 500) {
            this.player.setX(x);
            this.player.setY(y);
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

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyChar()) {
            case 'a':
                System.out.println("wciśniecie a");
                aKeyPressed = true;
                miniTimer.start();
//                System.out.println("You typed key char: " + e.getKeyChar() + " : " + e.getKeyCode());
//                System.out.println("Wciśnięcie a");
//                if (!collide(-Player.getVEL())) {
//                    this.player.moveLeft(Player.getVEL()); //ustawiło xVel na -5
//                    this.player.setRunning(true); //do zmiany sprita
//                }
                break;
            case 'd':
                System.out.println("wciśniecie d");
                dKeyPressed = true;
                miniTimer.start();
                //System.out.println("You typed key char: " + e.getKeyChar() + " : " + e.getKeyCode());
//                if (!collide(Player.getVEL())) {
//                    this.player.moveRight(Player.getVEL());
//                    this.player.setRunning(true); //do zmiany sprita
//                }
                break;
            case 'r':
                this.player.setX(330);
                this.player.setY(0);
                break;
            case 32:
                this.player.move(0, -1); // -1 usuwa buga i można skakać
                this.player.jump();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //System.out.println("You released key char: " + e.getKeyChar() + " : " + e.getKeyCode());
        switch (e.getKeyChar()) {
            case 'a':
                System.out.println("zwolnienie a");
                this.player.setxVel(0);
                this.player.setRunning(false); //do zmiany sprita
                aKeyPressed = false;
                miniTimer.stop();
                break;
            case 'd':
                System.out.println("Zwolnienie d");
                this.player.setxVel(0);
                this.player.setRunning(false); //do zmiany sprita
                dKeyPressed = false;
                miniTimer.stop();
                break;
        }
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