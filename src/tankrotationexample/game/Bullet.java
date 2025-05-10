package tankrotationexample.game;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Bullet {
    private float x;
    private float y;
    private float angle;
    private float size;
    private final float R = 4;  // Base speed of bullet
    private final BufferedImage bulletImage;
    private final Tank owner;
    private boolean active = true;
    private int damage;

    public Bullet(float x, float y, float angle, BufferedImage bulletImage, Tank owner, int damage, float size) {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.bulletImage = bulletImage;
        this.owner = owner;
        this.damage = damage;
        this.size = size;
    }

    public void update() {
        // Move bullet forward
        x += Math.round(R * Math.cos(Math.toRadians(angle)));
        y += Math.round(R * Math.sin(Math.toRadians(angle)));

        // Deactivate bullet if it goes off screen
        //if (x < 0 || x > GameConstants.GAME_SCREEN_WIDTH ||
        //        y < 0 || y > GameConstants.GAME_SCREEN_HEIGHT) {
        //    active = false;
        //}
    }

    public void drawImage(Graphics g) {
        if (!active) return;

        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), bulletImage.getWidth() * size / 2.0,
                bulletImage.getHeight() * size / 2.0);
        rotation.scale(size, size);  // Scale the bullet based on size modifier

        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.bulletImage, rotation, null);
    }

    public Rectangle getBounds() {
        return new Rectangle(
                (int)x,
                (int)y,
                (int)(bulletImage.getWidth() * size),
                (int)(bulletImage.getHeight() * size)
        );
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Tank getOwner() {
        return owner;
    }

    public int getDamage() {
        return damage;
    }
}