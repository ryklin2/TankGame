package tankrotationexample.game;

import java.awt.image.BufferedImage;

public class SlowEnemyPowerUp extends PowerUp {
    private static final float SPEED_REDUCER = 0.5f;
    private GameWorld gameWorld;  // Add this

    public SlowEnemyPowerUp(float x, float y, BufferedImage image) {
        super(x, y, image);
    }

    @Override
    public void applyEffect(Tank tank) {
        // Get the enemy tank and slow it down
        Tank enemyTank = tank.getGameWorld().getEnemyTank(tank);
        if (enemyTank != null) {
            enemyTank.setEnemySlowed(true);
        }
    }

    @Override
    public void removeEffect(Tank tank) {
        // Reset enemy tank's speed
        Tank enemyTank = tank.getGameWorld().getEnemyTank(tank);
        if (enemyTank != null) {
            enemyTank.setEnemySlowed(false);
        }
    }
}