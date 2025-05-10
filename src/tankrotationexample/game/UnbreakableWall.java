package tankrotationexample.game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UnbreakableWall {
    private int x;
    private int y;
    private BufferedImage wallImage;

    public UnbreakableWall(int x, int y, BufferedImage wallImage) {
        this.x = x;
        this.y = y;
        this.wallImage = wallImage;
    }

    public void drawImage(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.wallImage, x, y, null);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, wallImage.getWidth(), wallImage.getHeight());
    }

    public int getX() { return x; }
    public int getY() { return y; }
}