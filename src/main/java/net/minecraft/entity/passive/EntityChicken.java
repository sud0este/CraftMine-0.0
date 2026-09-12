package net.minecraft.entity.passive;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public final class EntityChicken extends EntityAnimal {
    public EntityChicken(World world, double x, double y, double z) { super(world, x, y, z, 4.0f); setSize(0.4f, 0.8f); moveSpeed = 0.065f; }
    @Override public int getRenderColor() { return 0xF4F4EE; }
    @Override protected void onDeath(DamageSource source) { world.spawnEntity(new EntityItem(world, posX, posY + 0.3, posZ, new ItemStack(ItemRegistry.FEATHER, 1))); }
}
