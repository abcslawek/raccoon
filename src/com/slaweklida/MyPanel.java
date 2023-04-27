package com.slaweklida;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
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
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class MyPanel extends JPanel implements ActionListener, KeyListener {

    private Image backgroundImage;
    private Image instructionImage;
    private Image gameOverImage;
    private Image winImage;
    final int PANEL_WIDTH = 1920;
    final int PANEL_HEIGHT = 1080;
    private int offsetX;
    private Timer timer;
    private Player player;
    private ArrayList<Heart> hearts;
    private ArrayList<Block> blocks;
    private ArrayList<Block> collidedBlocks;
    private HashMap<String, BufferedImage[]> sprites;
    private boolean gameOver = false;
    private boolean win = false;


    //obsługa przerwań z klawiatury
    private boolean aKeyPressed = false;
    private boolean dKeyPressed = false;
    private boolean ctrlKeyPressed = false;
    private final Timer miniTimer = new Timer(30, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (aKeyPressed) {
                if (!collide(-Player.getVEL()) && !gameOver && !win) {
                    player.moveLeft(Player.getVEL()); //ustawiło xVel na -5
                }
            }
            if (dKeyPressed) {
                if (!collide(Player.getVEL()) && !gameOver && !win) {
                    player.moveRight(Player.getVEL()); //ustawiło xVel na 5
                }
            }
        }
    });

    public MyPanel() {
        //zdarzenia z klawiatury
        this.addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        //reszta
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.timer = new Timer(23, this); //17
        this.timer.start();
        this.player = new Player(330, 100, 32, 32);
        this.blocks = new ArrayList<>();
        this.collidedBlocks = new ArrayList<>();
        this.hearts = new ArrayList<>();


        //tło
        this.backgroundImage = new ImageIcon("src/com/slaweklida/imgs/backgroundGrey.png").getImage();

        //informacje
        this.instructionImage = new ImageIcon("src/com/slaweklida/imgs/instructionImage.png").getImage();
        this.gameOverImage = new ImageIcon("src/com/slaweklida/imgs/gameOverImage.png").getImage();
        this.winImage = new ImageIcon("src/com/slaweklida/imgs/winImage.png").getImage();

        //serca
        for (int i = 1; i < this.player.getLifes() + 1; i++)
            this.hearts.add(new Heart(i * 32, 10, 32, 32));

        //tworzenie mapy
        this.blocks.add(new SolidBlock(0, 250, 100, 100, "blockImage"));
        this.blocks.add(new SolidBlock(100, 250, 100, 100, "blockImage"));
        this.blocks.add(new SolidBlock(200, 250, 100, 100, "blockImage"));
        this.blocks.add(new SolidBlock(300, 250, 100, 100, "blockImage"));
        this.blocks.add(new SolidBlock(400, 250, 100, 100, "blockImage"));
        this.blocks.add(new SolidBlock(500, 250, 100, 100, "blockImage"));
        this.blocks.add(new SolidBlock(600, 250, 100, 100, "blockImage"));
        this.blocks.add(new SolidBlock(600, 150, 100, 100, "blockImage"));
        this.blocks.add(new SolidBlock(800, 150, 100, 100, "blockImage"));
        this.blocks.add(new SolidBlock(1050, 50, 100, 100, "blockImage"));
        this.blocks.add(new SolidBlock(1150, 150, 100, 100, "blockImage"));
        this.blocks.add(new SolidBlock(1250, 250, 100, 100, "blockImage"));
        this.blocks.add(new SolidBlock(1350, 250, 100, 100, "blockImage"));
        this.blocks.add(new SolidBlock(1450, 250, 100, 100, "blockImage"));
        this.blocks.add(new EndBlock(1450, 150, 100, 100, "houseImage"));
        this.blocks.add(new FlyingBlock(100, 130, 60, 20, 2, 100, "flyingBlockImage"));
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
        //g2D.setPaint(Color.white);
        //g2D.drawRect(this.player.getMaskX() - this.offsetX, this.player.getMaskY(),
        //        this.player.getMaskWidth(), this.player.getMaskHeight());

        //bohater
        g2D.drawImage(this.player.getSprite(), this.player.getX() - this.offsetX, this.player.getY(), null);

        //bloki
        for (Block block : this.blocks) {
            g2D.drawImage(block.getImage(), block.getX() - this.offsetX, block.getY(), null);
        }

        //serca
        for (Heart heart : this.hearts) {
            g2D.drawImage(heart.getImage(), heart.getX(), heart.getY(), null);
        }

        //napisy gameOver win
        if (this.gameOver || this.win) {
            if (this.gameOver) g2D.drawImage(this.gameOverImage, 205, 165, null);
            else g2D.drawImage(this.winImage, 205, 165, null);
        }

        //instrukcja
        g2D.drawImage(this.instructionImage, 32, 46, null);

        //parametry
//        g2D.setPaint(new Color(153, 0, 255));
//        g2D.setFont(new Font("Calibri", Font.BOLD, 13));
//        g2D.drawString("X: " + this.player.getX(), 10, 50);
//        g2D.drawString("Y: " + this.player.getY(), 10, 60);
//        g2D.drawString("Mask X: " + this.player.getMaskX(), 10, 70);
//        g2D.drawString("Mask Y: " + this.player.getMaskY(), 10, 80);
//        g2D.drawString("xVel: " + this.player.getxVel(), 10, 90);
//        g2D.drawString("yVel: " + this.player.getyVel(), 10, 100);
//        g2D.drawString("Collided Blocks: " + this.collidedBlocks.size(), 10, 110);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //wczytanie klatek animacji do gracza
        this.sprites = loadSpriteSheets("sprites", "Raccoon", 32, 32, this.player.getDirection(), this.player);
        this.player.setSprites(this.sprites.get(this.player.getSpriteSheet()));

        handleVerticalCollision();
        handleHorizontalScrolling();
        handleRespawn(330, 100);
        handleGameOver();
        handleFlyingBlockMoving(this.blocks);

        this.player.loop(60); //jeśli isRunning to może się wykonać tylko raz
        repaint(); //narysowanie nowej klatki
    }

    public boolean collide(int vel) {
        this.player.moveMask(vel, 0);
        for (Block block : this.blocks) {
            if (isMaskColliding(this.player, block)) {
                this.player.moveMask(-vel, 0);
                this.player.setXVel(0); //po respawnie gdy się idzie w kierunku ściany to postać się zatrzymuje
                if (block instanceof EndBlock) {
                    playSound("win");
                    this.win = true;
                }
                return true;
            }
        }
        this.player.moveMask(-vel, 0);
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
        if (rightPlayerCornersX - this.offsetX >= 500 && this.player.getxVel() > 0 ||
                (this.player.getX() - this.offsetX <= 150 && this.player.getxVel() < 0)) {
            this.offsetX += this.player.getxVel();
        }
    }

    public void handleRespawn(int x, int y) {
        if (this.player.getY() >= 500) {
            this.player.looseLife(); //gracz traci jedno życie

            try {
                this.hearts.get(this.player.getLifes()).looseHealth(); //ostatnie na liście serce pustoszeje
            } catch (IndexOutOfBoundsException e) {
            }

            if (this.player.getLifes() > 0) {
                this.player.setX(x);
                this.player.setY(y);
                this.offsetX = 0;
            }
        }
    }

    public void handleGameOver() {
        if (this.player.getLifes() == 0) {
            playSound("gameOver");
            this.gameOver = true;
        }
    }

    public void restoreHearts() {
        for (Heart heart : this.hearts)
            heart.restoreHealth();
    }

    public void handleFlyingBlockMoving(ArrayList<Block> blocks){
        for(Block block : blocks){
            if(block instanceof FlyingBlock){
                int beginningPos = ((FlyingBlock) block).getBeginningXPosition();
                int currentPos = block.getX();
                int range = ((FlyingBlock) block).getRange();
                boolean flyingRight = ((FlyingBlock) block).isFlyingRight();
                if(currentPos <= range + beginningPos && flyingRight)
                    ((FlyingBlock) block).moveRight();
                else if(currentPos <= beginningPos && !flyingRight)
                    ((FlyingBlock) block).setFlyingRight(true);
                else {
                    ((FlyingBlock) block).setFlyingRight(false);
                    ((FlyingBlock) block).moveLeft();
                }
            }
        }
    }

    //audio
    public static synchronized void playSound(final String name) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    File file = new File("src/com/slaweklida/sounds/" + name + ".wav");
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioStream);
                    clip.start();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }

    public static BufferedImage[] flip(BufferedImage[] sprites) {
        for (int i = 0; i < sprites.length; i++) {
            BufferedImage sprite = sprites[i];
            AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
            tx.translate(-sprite.getWidth(null), 0);
            AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            sprites[i] = op.filter(sprite, null);
        }
        return sprites;
    }

    public HashMap<String, BufferedImage[]> loadSpriteSheets(String dir1, String dir2, int width, int height, String direction, Player player) {
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
            int amountOfSubimages = image.getWidth() / player.getWidth();
            subimages = new BufferedImage[amountOfSubimages];
            for (int i = 0; i < amountOfSubimages; i++) {
                subimages[i] = image.getSubimage(i * player.getWidth(), 0, player.getWidth(), player.getHeight());
            }
            if (direction.equals("left")) {
                subimages = flip(subimages);
            }
            allSprites.put(file.getName(), subimages);
        }
        return allSprites;
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyChar()) {
            case 'a':
                aKeyPressed = true;
                miniTimer.start();
                break;
            case 'd':
                dKeyPressed = true;
                miniTimer.start();
                break;
            case 'r':
                if (this.gameOver || this.win) {
                    this.player.setX(330);
                    this.player.setY(0);
                    this.offsetX = 0;
                    this.player = new Player(330, 100, 32, 32);
                    restoreHearts();
                    this.gameOver = false;
                    this.win = false;
                }
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
                this.player.setXVel(0);
                this.aKeyPressed = false;
                this.miniTimer.stop();
                break;
            case 'd':
                this.player.setXVel(0);
                this.dKeyPressed = false;
                this.miniTimer.stop();
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