package tankrotationexample.game;

import tankrotationexample.GameConstants;
import tankrotationexample.Launcher;
import tankrotationexample.menus.EndGamePanel;
import tankrotationexample.SoundPlayer;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.awt.geom.AffineTransform;
import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.RenderingHints;

public class GameWorld extends JPanel implements Runnable {

    private BufferedImage world;
    private BufferedImage backgroundImage;
    private Tank t1;
    private Tank t2;
    private final Launcher lf;
    private long tick = 0;
    private long lastPowerUpSpawnTime = 0;
    private static final long POWERUP_SPAWN_DELAY = 20000; // 20 seconds
    private List<Bullet> bullets = new ArrayList<>();
    private List<BreakableWall> breakableWalls = new ArrayList<>();
    private List<PowerUp> powerUps = new ArrayList<>();
    private static GameWorld instance;
    private Random random = new Random();
    private boolean gameOver = false;

    private List<UnbreakableWall> unbreakableWalls = new ArrayList<>();
    private static int currentLevel = 1;

    public static void setLevel(int level) {
        currentLevel = level;
    }


    public GameWorld(Launcher lf) {
        this.lf = lf;
    }

    public void addBullet(Bullet bullet) {
        bullets.add(bullet);
    }


    public Tank getEnemyTank(Tank currentTank) {
        return currentTank == t1 ? t2 : t1;
    }

    private void spawnPowerUp() {
        if (System.currentTimeMillis() - lastPowerUpSpawnTime < POWERUP_SPAWN_DELAY) {
            return;
        }

        // Remove any inactive power-ups
        powerUps.removeIf(p -> !p.isActive());

        // Only spawn if no active power-ups
        if (powerUps.isEmpty()) {
            try {
                BufferedImage rapidFireImg = ImageIO.read(Objects.requireNonNull(
                        GameWorld.class.getClassLoader().getResource("Pickup.gif")));
                BufferedImage bigShotImg = ImageIO.read(Objects.requireNonNull(
                        GameWorld.class.getClassLoader().getResource("Shell.gif")));
                BufferedImage slowEnemyImg = ImageIO.read(Objects.requireNonNull(
                        GameWorld.class.getClassLoader().getResource("Bouncing.gif")));

                // Random position (avoiding edges)
                int x = random.nextInt(GameConstants.GAME_SCREEN_WIDTH - 100) + 50;
                int y = random.nextInt(GameConstants.GAME_SCREEN_HEIGHT - 100) + 50;

                // Random power-up type
                PowerUp powerUp;
                switch (random.nextInt(3)) {
                    case 0 -> powerUp = new RapidFirePowerUp(x, y, rapidFireImg);
                    case 1 -> powerUp = new BigShotPowerUp(x, y, bigShotImg);
                    default -> powerUp = new SlowEnemyPowerUp(x, y, slowEnemyImg);
                }

                powerUps.add(powerUp);
                lastPowerUpSpawnTime = System.currentTimeMillis();
            } catch (IOException e) {
                System.out.println("Error loading power-up images: " + e.getMessage());
            }
        }
    }

    private void checkPowerUpCollisions() {
        for (PowerUp powerUp : new ArrayList<>(powerUps)) {
            if (powerUp.isActive()) {
                if (powerUp.getBounds().intersects(t1.getBounds())) {
                    t1.applyPowerUp(powerUp);
                    powerUp.setActive(false);
                } else if (powerUp.getBounds().intersects(t2.getBounds())) {
                    t2.applyPowerUp(powerUp);
                    powerUp.setActive(false);
                }
            }
        }
    }

    @Override
    public void run() {
        try {
            while (!gameOver) {
                this.tick++;
                this.t1.update();
                this.t2.update();

                // Spawn power-ups
                spawnPowerUp();

                // Update bullets
                for (Bullet bullet : new ArrayList<>(bullets)) {
                    bullet.update();
                }

                // Check for collisions
                checkCollisions();
                checkPowerUpCollisions();

                // Check for game over
                if (t1.getLives() <= 0) {
                    EndGamePanel.setWinner("Tank 2"); // Set Tank 2 as the winner
                    gameOver = true;
                    lf.setFrame(GameState.END); // Switch to the EndGamePanel
                }

                if (t2.getLives() <= 0) {
                    EndGamePanel.setWinner("Tank 1"); // Set Tank 1 as the winner
                    gameOver = true;
                    lf.setFrame(GameState.END);
                }

                this.repaint();
                Thread.sleep(1000 / 144);
            }
        } catch (InterruptedException ignored) {
            System.out.println(ignored);
        }
    }
    public void InitializeSplitScreenGame() {
        try {
            BufferedImage t1img = ImageIO.read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("tank1.png")));
            BufferedImage t2img = ImageIO.read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("tank2.png")));
            BufferedImage bulletImg = ImageIO.read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("Shell.gif")));
            BufferedImage breakableImg = ImageIO.read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("wall1.png")));
            BufferedImage unbreakableImg = ImageIO.read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("wall2.png")));

            // Set a larger map size
            this.world = new BufferedImage(GameConstants.SPLIT_SCREEN_MAP_WIDTH, GameConstants.SPLIT_SCREEN_MAP_HEIGHT, BufferedImage.TYPE_INT_RGB);

            // Initialize tanks
            t1 = new Tank(300, 300, 0, 0, 0, t1img, bulletImg, this);
            t2 = new Tank(GameConstants.SPLIT_SCREEN_MAP_WIDTH - 300, GameConstants.SPLIT_SCREEN_MAP_HEIGHT - 300, 0, 0, 180, t2img, bulletImg, this);

            // Initialize walls for split-screen level
            initializeWalls(breakableImg, unbreakableImg);

            // Clear other objects
            bullets.clear();
            powerUps.clear();

        } catch (IOException e) {
            System.out.println("Error loading images for split-screen game: " + e.getMessage());
        }
    }

    private void checkCollisions() {
        List<Bullet> bulletsToRemove = new ArrayList<>();
        List<BreakableWall> wallsToRemove = new ArrayList<>();

        for (Bullet bullet : bullets) {
            Rectangle bulletBounds = bullet.getBounds();

            // Check wall collisions
            for (BreakableWall wall : breakableWalls) {
                if (!wall.isDestroyed() && bulletBounds.intersects(wall.getBounds())) {
                    wall.damage(25);
                    bulletsToRemove.add(bullet);
                    SoundPlayer.playSound("Explosion_small.wav");  // Small explosion for wall hit
                    if (wall.isDestroyed()) {
                        wallsToRemove.add(wall);
                    }
                    break;
                }
            }

            for (UnbreakableWall wall : unbreakableWalls) {
                if (bulletBounds.intersects(wall.getBounds())) {
                    bulletsToRemove.add(bullet);
                    SoundPlayer.playSound("Explosion_small.wav");  // Small explosion for wall hit
                    break;
                }
            }

            // Tank collisions
            if (bullet.getOwner() != t1 && bulletBounds.intersects(t1.getBounds())) {
                t1.takeDamage(bullet.getDamage());
                bulletsToRemove.add(bullet);
                SoundPlayer.playSound("Explosion_small.wav");  // Small explosion for hit
                if (t1.getHealth() <= 0 && t1.getLives() > 0) {
                    SoundPlayer.playSound("Explosion_large.wav");  // Large explosion for life lost
                    respawnTank(t1);
                }
            }
            if (bullet.getOwner() != t2 && bulletBounds.intersects(t2.getBounds())) {
                t2.takeDamage(bullet.getDamage());
                bulletsToRemove.add(bullet);
                SoundPlayer.playSound("Explosion_small.wav");  // Small explosion for hit
                if (t2.getHealth() <= 0 && t2.getLives() > 0) {
                    SoundPlayer.playSound("Explosion_large.wav");  // Large explosion for life lost
                    respawnTank(t2);
                }
            }
        }


        // Check tank health and lives
        if (t1.getLives() <= 0) {
            EndGamePanel.setWinner("Tank 2");  // Declare Tank 2 as winner
            gameOver = true;
            lf.setFrame(GameState.END);
            lf.getEndGamePanel().updateWinnerDisplay();
        }

        if (t2.getLives() <= 0) {
            EndGamePanel.setWinner("Tank 1");  // Declare Tank 1 as winner
            gameOver = true;
            lf.setFrame(GameState.END);
            lf.getEndGamePanel().updateWinnerDisplay();
        }

        // Remove bullets and walls marked for removal
        bullets.removeAll(bulletsToRemove);
        breakableWalls.removeAll(wallsToRemove);
    }



    private void respawnTank(Tank tank) {
        if (tank == t1) {
            tank.setX(300);
            tank.setY(300);
            tank.setAngle(0);
        } else {
            tank.setX(GameConstants.GAME_SCREEN_WIDTH - 300);
            tank.setY(GameConstants.GAME_SCREEN_HEIGHT - 300);
            tank.setAngle(180);
        }
        tank.resetHealth();
        tank.setLastShootTime(System.currentTimeMillis()); // Add this to reset shooting cooldown
        bullets.clear();
    }

    public boolean isCollidingWithWalls(Tank tank, float newX, float newY) {
        Rectangle tankBounds = new Rectangle((int) newX, (int) newY, tank.getBounds().width, tank.getBounds().height);

        // Check collision with breakable walls
        for (BreakableWall wall : breakableWalls) {
            if (!wall.isDestroyed() && tankBounds.intersects(wall.getBounds())) {
                return true;
            }
        }

        // Check collision with unbreakable walls
        for (UnbreakableWall wall : unbreakableWalls) {
            if (tankBounds.intersects(wall.getBounds())) {
                return true;
            }
        }

        return false; // No collision detected
    }

    public void resetGame() {
        this.tick = 0;
        this.gameOver = false;
        respawnTank(t1);
        respawnTank(t2);
        this.bullets.clear();
        this.powerUps.clear();
        t1.resetLives();
        t2.resetLives();
    }

    public void InitializeGame() {
        Tank.tankCounter = 0;
        gameOver = false;

        // Remove old controls first
        for (KeyListener listener : this.lf.getJf().getKeyListeners()) {
            this.lf.getJf().removeKeyListener(listener);
        }

        // Clear all game objects
        bullets = new ArrayList<>();
        powerUps = new ArrayList<>();
        breakableWalls = new ArrayList<>();
        unbreakableWalls = new ArrayList<>();

        // Set world size dynamically based on game state (split-screen or standard)
        int mapWidth = Launcher.getInstance().getCurrentState() == GameState.SPLITSCREEN
                ? GameConstants.SPLIT_SCREEN_MAP_WIDTH
                : GameConstants.GAME_SCREEN_WIDTH;
        int mapHeight = Launcher.getInstance().getCurrentState() == GameState.SPLITSCREEN
                ? GameConstants.SPLIT_SCREEN_MAP_HEIGHT
                : GameConstants.GAME_SCREEN_HEIGHT;

        this.world = new BufferedImage(mapWidth, mapHeight, BufferedImage.TYPE_INT_RGB);

        try {
            // Load background
            this.backgroundImage = ImageIO.read(Objects.requireNonNull(
                    GameWorld.class.getClassLoader().getResource("Background.bmp")));
            if (this.backgroundImage != null) {
                System.out.println("Background image loaded successfully.");
                System.out.println("Background dimensions: " + backgroundImage.getWidth() + "x" + backgroundImage.getHeight());
            } else {
                System.out.println("Background image failed to load.");
            }

            // Load tank images
            BufferedImage t1img = ImageIO.read(Objects.requireNonNull(
                    GameWorld.class.getClassLoader().getResource("tank1.png")));
            BufferedImage t2img = ImageIO.read(Objects.requireNonNull(
                    GameWorld.class.getClassLoader().getResource("tank2.png")));

            // Load bullet image
            BufferedImage bulletImg = ImageIO.read(Objects.requireNonNull(
                    GameWorld.class.getClassLoader().getResource("Shell.gif")));

            // Load wall images
            BufferedImage breakableImg = ImageIO.read(Objects.requireNonNull(
                    GameWorld.class.getClassLoader().getResource("wall2.png")));
            BufferedImage unbreakableImg = ImageIO.read(Objects.requireNonNull(
                    GameWorld.class.getClassLoader().getResource("wall1.png")));

            // Initialize tanks with fresh instances
            t1 = new Tank(300, 300, 0, 0, 0, t1img, bulletImg, this);
            t2 = new Tank(mapWidth - 300, mapHeight - 300, 0, 0, 180, t2img, bulletImg, this);

            t1.setLastShootTime(System.currentTimeMillis());
            t2.setLastShootTime(System.currentTimeMillis());

            // Setup new controls
            TankControl tc1 = new TankControl(t1, KeyEvent.VK_W, KeyEvent.VK_S,
                    KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE);
            TankControl tc2 = new TankControl(t2, KeyEvent.VK_UP, KeyEvent.VK_DOWN,
                    KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_NUMPAD0);

            System.out.println("Adding controls for Tank 1: " + t1.getId());
            System.out.println("Adding controls for Tank 2: " + t2.getId());

            this.lf.getJf().addKeyListener(tc1);
            this.lf.getJf().addKeyListener(tc2);

            // Initialize walls
            if (Launcher.getInstance().getCurrentState() == GameState.GAME) {
                initializeWalls(breakableImg, unbreakableImg); // Standard mode
            } else if (Launcher.getInstance().getCurrentState() == GameState.SPLITSCREEN) {
                createLevel3(breakableImg, unbreakableImg, 40, 40); // Split-screen level with a barricade
            }
            SoundPlayer.playGameMusic();

        } catch (IOException ex) {
            System.out.println("Error loading assets: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    private void drawBackground(Graphics2D g2, int startX, int startY, int width, int height) {
        // Clear the area first
        g2.setColor(Color.BLACK);
        g2.fillRect(startX, startY, width, height);

        // Draw tiled background
        if (backgroundImage != null) {
            int tileWidth = backgroundImage.getWidth();
            int tileHeight = backgroundImage.getHeight();

            // Calculate the range to draw tiles
            int startTileX = startX / tileWidth * tileWidth;
            int startTileY = startY / tileHeight * tileHeight;
            int endX = startX + width;
            int endY = startY + height;

            for (int x = startTileX; x < endX; x += tileWidth) {
                for (int y = startTileY; y < endY; y += tileHeight) {
                    g2.drawImage(backgroundImage, x, y, null);
                }
            }
        }
    }


    private void initializeWalls(BufferedImage breakableImg, BufferedImage unbreakableImg) {
        int wallWidth = breakableImg.getWidth();
        int wallHeight = breakableImg.getHeight();

        // Clear existing walls
        breakableWalls.clear();
        unbreakableWalls.clear();

        switch (currentLevel) {
            case 1 -> createLevel1(breakableImg, unbreakableImg, wallWidth, wallHeight);
            case 2 -> createLevel2(breakableImg, unbreakableImg, wallWidth, wallHeight);
            case 3 -> createLevel3(breakableImg, unbreakableImg, wallWidth, wallHeight);
        }
    }
    private void createLevel1(BufferedImage breakableImg, BufferedImage unbreakableImg, int wallWidth, int wallHeight) {
        // Simple symmetric layout
        // Center walls
        breakableWalls.add(new BreakableWall(GameConstants.GAME_SCREEN_WIDTH/2 - wallWidth/2,
                GameConstants.GAME_SCREEN_HEIGHT/2 - wallHeight/2,
                breakableImg));

        // Corner unbreakable walls
        unbreakableWalls.add(new UnbreakableWall(100, 100, unbreakableImg));
        unbreakableWalls.add(new UnbreakableWall(GameConstants.GAME_SCREEN_WIDTH - 100 - wallWidth,
                100, unbreakableImg));
        unbreakableWalls.add(new UnbreakableWall(100,
                GameConstants.GAME_SCREEN_HEIGHT - 100 - wallHeight,
                unbreakableImg));
        unbreakableWalls.add(new UnbreakableWall(GameConstants.GAME_SCREEN_WIDTH - 100 - wallWidth,
                GameConstants.GAME_SCREEN_HEIGHT - 100 - wallHeight,
                unbreakableImg));
    }

    private void createLevel2(BufferedImage breakableImg, BufferedImage unbreakableImg, int wallWidth, int wallHeight) {
        // Clear existing walls
        breakableWalls.clear();
        unbreakableWalls.clear();

        // Define wall positions for two parallel walls
        int wall1X = GameConstants.GAME_SCREEN_WIDTH / 3 - wallWidth / 2; // Left wall
        int wall2X = 2 * GameConstants.GAME_SCREEN_WIDTH / 3 - wallWidth / 2; // Right wall

        // Add walls vertically for both columns
        for (int y = 150; y < GameConstants.GAME_SCREEN_HEIGHT - 150; y += wallHeight) {
            if ((y / wallHeight) % 3 == 0) {
                // First row in the pattern: Add unbreakable walls
                unbreakableWalls.add(new UnbreakableWall(wall1X, y, unbreakableImg));
                unbreakableWalls.add(new UnbreakableWall(wall2X, y, unbreakableImg));
            } else {
                // Second and third rows in the pattern: Add breakable walls
                breakableWalls.add(new BreakableWall(wall1X, y, breakableImg));
                breakableWalls.add(new BreakableWall(wall2X, y, breakableImg));
            }
        }
    }





    private void createLevel3(BufferedImage breakableImg, BufferedImage unbreakableImg, int wallWidth, int wallHeight) {
        // Clear existing walls
        breakableWalls.clear();
        unbreakableWalls.clear();

        // Define the barricade dimensions
        int barricadeWidth = GameConstants.SPLIT_SCREEN_MAP_WIDTH - 400; // Leave space on edges
        int barricadeHeight = 200; // Thickness of the barricade
        int barricadeStartX = 200; // Starting x-coordinate for the barricade
        int barricadeStartY = GameConstants.SPLIT_SCREEN_MAP_HEIGHT / 2 - barricadeHeight / 2; // Centered vertically

        // Fill the barricade area with breakable walls
        for (int x = barricadeStartX; x < barricadeStartX + barricadeWidth; x += wallWidth) {
            for (int y = barricadeStartY; y < barricadeStartY + barricadeHeight; y += wallHeight) {
                breakableWalls.add(new BreakableWall(x, y, breakableImg));
            }
        }
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Set rendering hints for better quality
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        if (Launcher.getInstance().getCurrentState() == GameState.SPLITSCREEN) {
            renderSplitScreen(g2);
        } else {
            renderStandardView(g2);
        }
    }
    // Tessellate background across the game world
    private void drawTessellatedBackground(Graphics2D g2) {
        if (backgroundImage == null) {
            System.out.println("Background image is null in drawTessellatedBackground");
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, getWidth(), getHeight());
            return;
        }

        int tileWidth = backgroundImage.getWidth();
        int tileHeight = backgroundImage.getHeight();

        // Make sure we're drawing with full opacity
        Composite originalComposite = g2.getComposite();
        g2.setComposite(AlphaComposite.SrcOver);

        // Draw tiles
        for (int x = 0; x < getWidth(); x += tileWidth) {
            for (int y = 0; y < getHeight(); y += tileHeight) {
                g2.drawImage(backgroundImage, x, y, tileWidth, tileHeight, null);
            }
        }

        // Restore original composite
        g2.setComposite(originalComposite);
    }

    // Standard rendering for single-screen gameplay
    private void renderStandardView(Graphics2D g2) {
        // Create buffer for standard view
        Graphics2D buffer = world.createGraphics();
        try {
            // Draw background for full screen
            drawBackground(buffer, 0, 0, GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT);

            // Draw game elements
            drawGameElements(buffer);

            // Draw to screen
            g2.drawImage(world, 0, 0, null);

            // Draw minimap on top
            drawMinimap(g2);
        } finally {
            buffer.dispose();
        }
    }

    private void renderSplitScreen(Graphics2D g2) {
        // Calculate view boundaries for each tank
        int t1ViewX = Math.max(0, Math.min((int)t1.getX() - GameConstants.SPLIT_SCREEN_VIEW_WIDTH/2,
                GameConstants.SPLIT_SCREEN_MAP_WIDTH - GameConstants.SPLIT_SCREEN_VIEW_WIDTH));
        int t1ViewY = Math.max(0, Math.min((int)t1.getY() - GameConstants.SPLIT_SCREEN_VIEW_HEIGHT/2,
                GameConstants.SPLIT_SCREEN_MAP_HEIGHT - GameConstants.SPLIT_SCREEN_VIEW_HEIGHT));

        int t2ViewX = Math.max(0, Math.min((int)t2.getX() - GameConstants.SPLIT_SCREEN_VIEW_WIDTH/2,
                GameConstants.SPLIT_SCREEN_MAP_WIDTH - GameConstants.SPLIT_SCREEN_VIEW_WIDTH));
        int t2ViewY = Math.max(0, Math.min((int)t2.getY() - GameConstants.SPLIT_SCREEN_VIEW_HEIGHT/2,
                GameConstants.SPLIT_SCREEN_MAP_HEIGHT - GameConstants.SPLIT_SCREEN_VIEW_HEIGHT));

        // Draw left view (Tank 1)
        Graphics2D g2Left = (Graphics2D) g2.create(0, 0,
                GameConstants.SPLIT_SCREEN_VIEW_WIDTH, GameConstants.SPLIT_SCREEN_VIEW_HEIGHT);
        try {
            g2Left.translate(-t1ViewX, -t1ViewY);
            drawBackground(g2Left, t1ViewX, t1ViewY,
                    GameConstants.SPLIT_SCREEN_VIEW_WIDTH, GameConstants.SPLIT_SCREEN_VIEW_HEIGHT);
            drawGameElements(g2Left);
        } finally {
            g2Left.dispose();
        }

        // Draw right view (Tank 2)
        Graphics2D g2Right = (Graphics2D) g2.create(GameConstants.SPLIT_SCREEN_VIEW_WIDTH, 0,
                GameConstants.SPLIT_SCREEN_VIEW_WIDTH, GameConstants.SPLIT_SCREEN_VIEW_HEIGHT);
        try {
            g2Right.translate(-t2ViewX, -t2ViewY);
            drawBackground(g2Right, t2ViewX, t2ViewY,
                    GameConstants.SPLIT_SCREEN_VIEW_WIDTH, GameConstants.SPLIT_SCREEN_VIEW_HEIGHT);
            drawGameElements(g2Right);
        } finally {
            g2Right.dispose();
        }

        // Draw divider
        g2.setColor(Color.BLACK);
        g2.fillRect(GameConstants.SPLIT_SCREEN_VIEW_WIDTH - 2, 0, 4, GameConstants.SPLIT_SCREEN_VIEW_HEIGHT);
    }

    private void drawMinimap(Graphics2D g2) {
        int minimapWidth = 200;
        int minimapHeight = 150;
        int minimapX = GameConstants.GAME_SCREEN_WIDTH - minimapWidth - 20;
        int minimapY = 20;

        // Draw minimap background with transparency
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
        g2.setColor(new Color(0, 50, 0));
        g2.fillRect(minimapX, minimapY, minimapWidth, minimapHeight);

        // Calculate scale factors
        float scaleX = (float) minimapWidth / GameConstants.GAME_SCREEN_WIDTH;
        float scaleY = (float) minimapHeight / GameConstants.GAME_SCREEN_HEIGHT;

        // Draw walls on minimap
        g2.setColor(Color.GRAY);
        for (UnbreakableWall wall : unbreakableWalls) {
            int miniX = minimapX + (int)(wall.getX() * scaleX);
            int miniY = minimapY + (int)(wall.getY() * scaleY);
            g2.fillRect(miniX, miniY, 4, 4);
        }

        g2.setColor(Color.ORANGE);
        for (BreakableWall wall : breakableWalls) {
            int miniX = minimapX + (int)(wall.getX() * scaleX);
            int miniY = minimapY + (int)(wall.getY() * scaleY);
            g2.fillRect(miniX, miniY, 4, 4);
        }

        // Draw tanks on minimap
        g2.setColor(Color.RED);
        g2.fillOval(minimapX + (int)(t1.getX() * scaleX),
                minimapY + (int)(t1.getY() * scaleY), 6, 6);

        g2.setColor(Color.BLUE);
        g2.fillOval(minimapX + (int)(t2.getX() * scaleX),
                minimapY + (int)(t2.getY() * scaleY), 6, 6);

        // Reset composite for normal drawing
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

        // Draw minimap border
        g2.setColor(Color.WHITE);
        g2.drawRect(minimapX, minimapY, minimapWidth, minimapHeight);
    }

    // Helper method to draw all game elements
    private void drawGameElements(Graphics2D g2) {
        // Draw walls
        for (UnbreakableWall wall : unbreakableWalls) {
            wall.drawImage(g2);
        }
        for (BreakableWall wall : breakableWalls) {
            wall.drawImage(g2);
        }

        // Draw power-ups
        for (PowerUp powerUp : powerUps) {
            powerUp.drawImage(g2);
        }

        // Draw tanks
        t1.drawImage(g2);
        t2.drawImage(g2);

        // Draw bullets
        for (Bullet bullet : bullets) {
            bullet.drawImage(g2);
        }
    }




}