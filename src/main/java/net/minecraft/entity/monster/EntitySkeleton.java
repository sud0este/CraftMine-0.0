package net.minecraft.entity.monster;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public final class EntitySkeleton extends EntityMob {
    private int arrowCooldown;
    public EntitySkeleton(World world, double x, double y, double z) {
        super(world, x, y, z, 20.0f);
        setSize(0.6f, 1.99f);
        moveSpeed = 0.065f;
        attackDamage = 2.0f;
    }
    @Override protected void onLivingUpdate() {
        super.onLivingUpdate();
        if (attackTarget != null && getDistanceSq(attackTarget) < 12 * 12 && arrowCooldown-- <= 0) {
            world.spawnEntity(new EntityArrow(world, this, new net.minecraft.util.math.Vec3(
                    attackTarget.posX - posX, attackTarget.posY + attackTarget.getEyeHeight() - (posY + getEyeHeight()), attackTarget.posZ - posZ)));
            arrowCooldown = 35;
        }
    }
    @Override public int getRenderColor() { return 0xD8D8D1; }
}
