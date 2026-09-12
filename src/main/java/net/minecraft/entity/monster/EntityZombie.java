package net.minecraft.entity.monster;

import net.minecraft.world.World;

public final class EntityZombie extends EntityMob {
    public EntityZombie(World world, double x, double y, double z) {
        super(world, x, y, z, 20.0f);
        setSize(0.6f, 1.95f);
        moveSpeed = 0.075f;
        attackDamage = 3.0f;
    }
    @Override public int getRenderColor() { return 0x4C8F51; }
}
