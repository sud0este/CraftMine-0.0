package net.minecraft.entity.monster;

import net.minecraft.world.World;

public final class EntitySpider extends EntityMob {
    public EntitySpider(World world, double x, double y, double z) {
        super(world, x, y, z, 16.0f);
        setSize(1.4f, 0.9f);
        moveSpeed = 0.095f;
        attackDamage = 2.0f;
    }
    @Override public int getRenderColor() { return 0x2A1C1B; }
}
