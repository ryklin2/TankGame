package tankrotationexample.game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BreakableWall {
    private int x;
    private int y;
    private BufferedImage wallImage;
    private int health = 100;
    private boolean destroyed = false;

    public BreakableWall(int x, int y, BufferedImage wallImage) {
        this.x = x;
        this.y = y;
        this.wallImage = wallImage;
    }



    public void damage(int amount) {
        health -= amount;
        if(health <= 0) {
            destroyed = true;
        }
    }

    public void drawImage(Graphics g) {
        if(!destroyed) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawImage(this.wallImage, x, y, null);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, wallImage.getWidth(), wallImage.getHeight());
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public int getX() { return x; }
    public int getY() { return y; }
}