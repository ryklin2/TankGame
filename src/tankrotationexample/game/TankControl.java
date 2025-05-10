package tankrotationexample.game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Controls for tank movement and actions
 */
public class TankControl implements KeyListener {
    private final Tank tank;
    private final int up;
    private final int down;
    private final int right;
    private final int left;
    private final int shoot;

    /**
     * Constructor for tank controls
     * @param tank The tank to be controlled
     * @param up Key for forward movement
     * @param down Key for backward movement
     * @param left Key for left rotation
     * @param right Key for right rotation
     * @param shoot Key for shooting
     */
    public TankControl(Tank tank, int up, int down, int left, int right, int shoot) {
        this.tank = tank;
        this.up = up;
        this.down = down;
        this.right = right;
        this.left = left;
        this.shoot = shoot;
    }

    @Override
    public void keyTyped(KeyEvent ke) {
        // Not used but required by KeyListener interface
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        int keyPressed = ke.getKeyCode();
        switch (keyPressed) {
            case KeyEvent.VK_W, KeyEvent.VK_UP -> {
                if (keyPressed == up) {
                    this.tank.toggleUpPressed();
                }
            }
            case KeyEvent.VK_S, KeyEvent.VK_DOWN -> {
                if (keyPressed == down) {
                    this.tank.toggleDownPressed();
                }
            }
            case KeyEvent.VK_A, KeyEvent.VK_LEFT -> {
                if (keyPressed == left) {
                    this.tank.toggleLeftPressed();
                }
            }
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> {
                if (keyPressed == right) {
                    this.tank.toggleRightPressed();
                }
            }
            case KeyEvent.VK_SPACE, KeyEvent.VK_NUMPAD0 -> {
                if (keyPressed == shoot) {
                    this.tank.shoot();  // We'll need to add this method to Tank class
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        int keyReleased = ke.getKeyCode();
        switch (keyReleased) {
            case KeyEvent.VK_W, KeyEvent.VK_UP -> {
                if (keyReleased == up) {
                    this.tank.unToggleUpPressed();
                }
            }
            case KeyEvent.VK_S, KeyEvent.VK_DOWN -> {
                if (keyReleased == down) {
                    this.tank.unToggleDownPressed();
                }
            }
            case KeyEvent.VK_A, KeyEvent.VK_LEFT -> {
                if (keyReleased == left) {
                    this.tank.unToggleLeftPressed();
                }
            }
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> {
                if (keyReleased == right) {
                    this.tank.unToggleRightPressed();
                }
            }
        }
    }
}