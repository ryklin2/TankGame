package tankrotationexample.game;

import tankrotationexample.SoundPlayer;
import tankrotationexample.GameConstants;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;


public class Tank {
    // Position and movement
    private float x;
    private float y;
    private float vx;
    private float vy;
    private float angle;

    // Speed and rotation constants
    private float R = 5;
    private final float defaultSpeed = 5.0f;
    private float movementSpeed = defaultSpeed;
    private final float ROTATIONSPEED = 3.0f;

    // Shooting related variables
    private long lastShootTime = 0;
    private long shootCooldown = 1000; // Default 1 second
    private final long defaultCooldown = 1000;
    private float bulletSize = 1.0f;
    private int bulletDamage = 10;

    // Power-up related
    private PowerUp activePowerUp = null;
    private long powerUpEndTime = 0;

    // Images
    private final BufferedImage img;
    private final BufferedImage bulletImage;

    // Controls
    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;

    // Health system
    private int health = 100;
    private int lives = 3;
    private static final int MAX_HEALTH = 100;

    private final GameWorld gameWorld;
    private final String id;
    public static int tankCounter = 0;

    private boolean enemySlowed = false;
    private final float DEFAULT_MOVEMENT_SPEED = 5.0f;
    private final float DEFAULT_ROTATION_SPEED = 3.0f;



    Tank(float x, float y, float vx, float vy, float angle, BufferedImage img, BufferedImage bulletImage, GameWorld gameWorld) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.img = img;
        this.angle = angle;
        this.bulletImage = bulletImage;
        this.gameWorld = gameWorld;
        this.id = "Tank_" + (++tankCounter);
    }

    public void shoot() {
        if (System.currentTimeMillis() - lastShootTime >= shootCooldown) {
            float bulletX = x + img.getWidth()/2.0f - (bulletImage.getWidth() * bulletSize)/2.0f;
            float bulletY = y + img.getHeight()/2.0f - (bulletImage.getHeight() * bulletSize)/2.0f;

            Bullet bullet = new Bullet(bulletX, bulletY, angle, bulletImage, this, bulletDamage, bulletSize);
            gameWorld.addBullet(bullet);

            SoundPlayer.playSound("Explosion_small.wav");

            lastShootTime = System.currentTimeMillis();
        }
    }

    public GameWorld getGameWorld() {
        return gameWorld;
    }
    public void setLastShootTime(long time) {
        this.lastShootTime = time;
    }

    public String getId() {
        return id;
    }
    public void setEnemySlowed(boolean slowed) {
        this.enemySlowed = slowed;
    }


    public void loseLife() {
        lives--;
        health = 100; // Reset health when losing a life
    }


    // Power-up methods
    public void applyPowerUp(PowerUp powerUp) {
        if (activePowerUp != null) {
            activePowerUp.removeEffect(this);
        }
        activePowerUp = powerUp;
        powerUp.applyEffect(this);
        powerUpEndTime = System.currentTimeMillis() + PowerUp.DURATION;
    }

    // Getters and setters for power-ups
    public void setShootCooldown(long cooldown) { this.shootCooldown = cooldown; }
    public long getDefaultCooldown() { return defaultCooldown; }
    public void setBulletSize(float size) { this.bulletSize = size; }
    public void setBulletDamage(int damage) { this.bulletDamage = damage; }
    public void setMovementSpeed(float speed) { this.movementSpeed = speed; }
    public float getDefaultSpeed() { return defaultSpeed; }

    // Health and lives management
    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health <= 0) {
            this.lives--;
            if (this.lives > 0) {
                this.health = MAX_HEALTH;
            }
        }
    }

    public void resetHealth() {
        this.health = MAX_HEALTH;
    }

    public void resetLives() {
        this.lives = 3;
        this.health = MAX_HEALTH;
    }

    // Position getters/setters
    void setX(float x) { this.x = x; }
    void setY(float y) { this.y = y; }
    public float getX() { return x; }
    public float getY() { return y; }
    public void setAngle(float angle) { this.angle = angle; }
    public int getHealth() { return health; }
    public int getLives() { return lives; }

    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, img.getWidth(), img.getHeight());
    }

    // Movement controls
    void toggleUpPressed() { this.UpPressed = true; }
    void toggleDownPressed() { this.DownPressed = true; }
    void toggleRightPressed() { this.RightPressed = true; }
    void toggleLeftPressed() { this.LeftPressed = true; }
    void unToggleUpPressed() { this.UpPressed = false; }
    void unToggleDownPressed() { this.DownPressed = false; }
    void unToggleRightPressed() { this.RightPressed = false; }
    void unToggleLeftPressed() { this.LeftPressed = false; }

    void update() {
        // Movement updates
        if (this.LeftPressed) {
            this.rotateLeft();
        }
        if (this.RightPressed) {
            this.rotateRight();
        }
        else if (!this.LeftPressed && !this.RightPressed) {
            if (this.UpPressed) {
                this.moveForwards();
            }
            if (this.DownPressed) {
                this.moveBackwards();
            }
        }


        // Check if power-up has expired
        if (activePowerUp != null && System.currentTimeMillis() > powerUpEndTime) {
            activePowerUp.removeEffect(this);
            activePowerUp = null;
        }
    }

    private void rotateLeft() {
        float currentRotationSpeed = enemySlowed ? DEFAULT_ROTATION_SPEED * 0.5f : DEFAULT_ROTATION_SPEED;
        this.angle -= currentRotationSpeed;
    }

    private void rotateRight() {
        float currentRotationSpeed = enemySlowed ? DEFAULT_ROTATION_SPEED * 0.5f : DEFAULT_ROTATION_SPEED;
        this.angle += currentRotationSpeed;
    }

    private void moveForwards() {
        float currentSpeed = enemySlowed ? DEFAULT_MOVEMENT_SPEED * 0.5f : DEFAULT_MOVEMENT_SPEED;
        float tempX = x + Math.round(currentSpeed * Math.cos(Math.toRadians(angle)));
        float tempY = y + Math.round(currentSpeed * Math.sin(Math.toRadians(angle)));

        if (!gameWorld.isCollidingWithWalls(this, tempX, tempY)) { // Check collision before moving
            x = tempX;
            y = tempY;
        }
    }

    private void moveBackwards() {
        float currentSpeed = enemySlowed ? DEFAULT_MOVEMENT_SPEED * 0.5f : DEFAULT_MOVEMENT_SPEED;
        float tempX = x - Math.round(currentSpeed * Math.cos(Math.toRadians(angle)));
        float tempY = y - Math.round(currentSpeed * Math.sin(Math.toRadians(angle)));

        if (!gameWorld.isCollidingWithWalls(this, tempX, tempY)) { // Check collision before moving
            x = tempX;
            y = tempY;
        }
    }


    private void checkBorder() {
        if (x < 30) {
            x = 30;
        }
        if (x >= GameConstants.GAME_SCREEN_WIDTH - 88) {
            x = GameConstants.GAME_SCREEN_WIDTH - 88;
        }
        if (y < 40) {
            y = 40;
        }
        if (y >= GameConstants.GAME_SCREEN_HEIGHT - 80) {
            y = GameConstants.GAME_SCREEN_HEIGHT - 80;
        }
    }

    @Override
    public String toString() {
        return "x=" + x + ", y=" + y + ", angle=" + angle;
    }

    void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, rotation, null);

        // Draw health bar
        g2d.setColor(Color.RED);
        g2d.fillRect((int)x, (int)y - 10, img.getWidth(), 5);
        g2d.setColor(Color.GREEN);
        g2d.fillRect((int)x, (int)y - 10, (int)(img.getWidth() * (health/100.0)), 5);

        // Draw lives indicator
        g2d.setColor(Color.WHITE);
        for (int i = 0; i < lives; i++) {
            g2d.fillRect((int)x + (i * 15), (int)y - 20, 10, 10);
        }


        // Draw power-up indicator if active
        if (activePowerUp != null) {
            g2d.setColor(Color.YELLOW);
            g2d.drawString("POWER-UP!", (int)x, (int)y - 30);
        }
    }
    public void reset() {
        health = 100;
        lives = 3;
    }

}