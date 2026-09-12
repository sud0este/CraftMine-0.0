package net.minecraft.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.RayTraceResult;
import net.minecraft.util.math.Vec3;
import net.minecraft.world.World;

public final class EntityArrow extends Entity {
    private final EntityLivingBase shooter;
    private int life;
    public EntityArrow(World world, EntityLivingBase shooter, Vec3 direction) {
        super(world, shooter.posX, shooter.posY + shooter.getEyeHeight(), shooter.posZ);
        this.shooter = shooter; setSize(0.12f, 0.12f);
        Vec3 velocity = direction.normalize().scale(1.8); motionX = velocity.x; motionY = velocity.y; motionZ = velocity.z;
    }
    @Override public void onUpdate() {
        super.onUpdate();
        Vec3 start = new Vec3(posX, posY, posZ), end = start.add(motionX, motionY, motionZ);
        RayTraceResult hit = world.rayTraceBlocks(start, end, false);
        if (hit.type == RayTraceResult.Type.BLOCK) { setDead(); return; }
        for (EntityLivingBase target : world.getEntitiesWithinAABB(EntityLivingBase.class, getBoundingBox().expand(1, 1, 1), shooter)) {
            if (target.attackEntityFrom(DamageSource.GENERIC, 4.0f)) { setDead(); return; }
        }
        motionY -= 0.025; move(motionX, motionY, motionZ); motionX *= 0.99; motionY *= 0.99; motionZ *= 0.99;
        if (++life > 200) setDead();
    }
    @Override public int getRenderColor() { return 0x8B5A2B; }
}
