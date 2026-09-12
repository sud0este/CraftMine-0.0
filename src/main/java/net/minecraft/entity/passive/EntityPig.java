package net.minecraft.entity.passive;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public final class EntityPig extends EntityAnimal {
    public EntityPig(World world, double x, double y, double z) { super(world, x, y, z, 10.0f); setSize(0.9f, 0.9f); }
    @Override public int getRenderColor() { return 0xE69A9A; }
    @Override protected void onDeath(DamageSource source) { world.spawnEntity(new EntityItem(world, posX, posY + 0.3, posZ, new ItemStack(ItemRegistry.PORKCHOP, 1 + random.nextInt(2)))); }
}
