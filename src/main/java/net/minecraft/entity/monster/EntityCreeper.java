package net.minecraft.entity.monster;

import net.minecraft.world.World;

public final class EntityCreeper extends EntityMob {
    private int fuse;
    public EntityCreeper(World world, double x, double y, double z) {
        super(world, x, y, z, 20.0f);
        setSize(0.6f, 1.7f);
        moveSpeed = 0.075f;
        attackDamage = 0;
    }
    @Override protected void onLivingUpdate() {
        super.onLivingUpdate();
        if (attackTarget != null && getDistanceSq(attackTarget) < 3.0 * 3.0) {
            if (++fuse > 30) { world.createExplosion(this, posX, posY + 0.8, posZ, 3.0f); setDead(); }
        } else if (fuse > 0) fuse--;
    }
    @Override public int getRenderColor() { return fuse > 0 ? 0xF1F1E8 : 0x5CBA5C; }
}
