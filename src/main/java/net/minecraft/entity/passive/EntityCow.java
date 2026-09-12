package net.minecraft.entity.passive;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public final class EntityCow extends EntityAnimal {
    public EntityCow(World world, double x, double y, double z) { super(world, x, y, z, 10.0f); setSize(0.9f, 1.4f); }
    @Override public int getRenderColor() { return 0x5B3F2D; }
    @Override protected void onDeath(DamageSource source) { world.spawnEntity(new EntityItem(world, posX, posY + 0.3, posZ, new ItemStack(ItemRegistry.RAW_BEEF, 1 + random.nextInt(2)))); }
}
