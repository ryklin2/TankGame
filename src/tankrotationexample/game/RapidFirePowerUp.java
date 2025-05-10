package tankrotationexample.game;

import java.awt.image.BufferedImage;

public class RapidFirePowerUp extends PowerUp {
    private static final float SPEED_MULTIPLIER = 0.5f; // Reduces cooldown by half

    public RapidFirePowerUp(float x, float y, BufferedImage image) {
        super(x, y, image);
    }

    @Override
    public void applyEffect(Tank tank) {
        tank.setShootCooldown((long)(tank.getDefaultCooldown() * SPEED_MULTIPLIER));
    }

    @Override
    public void removeEffect(Tank tank) {
        tank.setShootCooldown(tank.getDefaultCooldown());
    }
}