package net.minecraft.entity.passive;

import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class EntityAnimal extends EntityLivingBase {
    protected final Random random = new Random();
    private int wanderTicks;
    private double wanderYaw;
    protected float moveSpeed = 0.055f;

    public EntityAnimal(World world, double x, double y, double z, float health) {
        super(world, health);
        setPosition(x, y, z);
    }

    @Override protected void onLivingUpdate() {
        if (wanderTicks-- <= 0) { wanderTicks = 30 + random.nextInt(80); wanderYaw = random.nextDouble() * Math.PI * 2.0; }
        motionX += Math.sin(wanderYaw) * moveSpeed * 0.3;
        motionZ += Math.cos(wanderYaw) * moveSpeed * 0.3;
        rotationYaw = (float) (wanderYaw * 180.0 / Math.PI);
        if (collidedHorizontally) jump();
    }
}
