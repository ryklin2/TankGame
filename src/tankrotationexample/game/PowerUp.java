package tankrotationexample.game;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class PowerUp {
    protected float x, y;
    protected BufferedImage image;
    protected boolean isActive = true;
    protected static final int DURATION = 5000; // 5 seconds

    public PowerUp(float x, float y, BufferedImage image) {
        this.x = x;
        this.y = y;
        this.image = image;
    }

    public abstract void applyEffect(Tank tank);
    public abstract void removeEffect(Tank tank);

    public void drawImage(Graphics g) {
        if (isActive) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawImage(this.image, (int)x, (int)y, null);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, image.getWidth(), image.getHeight());
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }
}