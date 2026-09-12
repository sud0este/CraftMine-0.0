package net.minecraft.entity.monster;

import net.minecraft.world.World;

public final class EntityEnderman extends EntityMob {
    public EntityEnderman(World world, double x, double y, double z) { super(world, x, y, z, 40.0f); setSize(0.6f, 2.9f); moveSpeed = 0.10f; attackDamage = 7.0f; }
    @Override public int getRenderColor() { return 0x24203F; }
}
