package tankrotationexample.game;

import java.awt.image.BufferedImage;

public class BigShotPowerUp extends PowerUp {
    private static final float SIZE_MULTIPLIER = 2.0f;

    public BigShotPowerUp(float x, float y, BufferedImage image) {
        super(x, y, image);
    }

    @Override
    public void applyEffect(Tank tank) {
        tank.setBulletSize(SIZE_MULTIPLIER);
        tank.setBulletDamage(20); // Double damage for bigger shots
    }

    @Override
    public void removeEffect(Tank tank) {
        tank.setBulletSize(1.0f);
        tank.setBulletDamage(10); // Reset to default damage
    }
}