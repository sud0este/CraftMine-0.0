package net.minecraft.entity.monster;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.PathNavigate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityMob extends EntityLivingBase {
    protected final Random random = new Random();
    protected float moveSpeed = 0.08f;
    protected float attackDamage = 2.0f;
    protected EntityPlayer attackTarget;
    protected final PathNavigate navigator;
    private int wanderTicks;
    private double wanderYaw;

    public EntityMob(World world, double x, double y, double z, float health) {
        super(world, health);
        setPosition(x, y, z);
        navigator = new PathNavigate(this);
    }

    @Override protected void onLivingUpdate() {
        EntityPlayer player = world.getPlayer();
        if (player != null && player.isEntityAlive() && getDistanceSq(player) < 18 * 18) attackTarget = player;
        if (attackTarget != null && (!attackTarget.isEntityAlive() || getDistanceSq(attackTarget) > 24 * 24)) attackTarget = null;
        if (attackTarget != null) {
            double dx = attackTarget.posX - posX, dz = attackTarget.posZ - posZ;
            double distance = Math.sqrt(dx * dx + dz * dz);
            if (distance > 2.2) {
                if (collidedHorizontally) navigator.tryMoveTo(attackTarget.posX, attackTarget.posY, attackTarget.posZ, moveSpeed);
                motionX += dx / Math.max(1, distance) * moveSpeed;
                motionZ += dz / Math.max(1, distance) * moveSpeed;
                rotationYaw = (float) (Math.atan2(-dx, dz) * 180.0 / Math.PI);
            } else if (attackTime == 0) {
                attackTarget.attackEntityFrom(DamageSource.MOB, attackDamage);
                attackTime = 20;
            }
        } else {
            if (wanderTicks-- <= 0) { wanderTicks = 20 + random.nextInt(80); wanderYaw = random.nextDouble() * Math.PI * 2.0; }
            motionX += Math.sin(wanderYaw) * moveSpeed * 0.35;
            motionZ += Math.cos(wanderYaw) * moveSpeed * 0.35;
            rotationYaw = (float) (wanderYaw * 180.0 / Math.PI);
        }
        if (collidedHorizontally) jump();
    }
}
