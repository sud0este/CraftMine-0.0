package net.minecraft.entity.passive;

import net.minecraft.world.World;

public final class EntitySquid extends EntityAnimal {
    public EntitySquid(World world, double x, double y, double z) { super(world, x, y, z, 10.0f); setSize(0.8f, 0.8f); moveSpeed = 0.03f; }
    @Override public int getRenderColor() { return 0x443D8A; }
}
