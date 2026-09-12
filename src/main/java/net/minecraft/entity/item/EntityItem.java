package net.minecraft.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public final class EntityItem extends Entity {
    private final ItemStack stack;
    private int age;

    public EntityItem(World world, double x, double y, double z, ItemStack stack) {
        super(world, x, y, z);
        this.stack = stack;
        setSize(0.25f, 0.25f);
        motionY = 0.20;
    }

    public ItemStack getItem() { return stack; }

    @Override public void onUpdate() {
        super.onUpdate();
        age++;
        motionY -= 0.04;
        move(motionX, motionY, motionZ);
        motionX *= 0.98; motionZ *= 0.98;
        if (onGround) motionY *= -0.35;
        EntityPlayer player = world.getPlayer();
        if (player != null && getDistanceSq(player) < 2.0) {
            if (player.getInventory().addItem(stack)) setDead();
        }
        if (age > 6000) setDead();
    }

    @Override public int getRenderColor() { return stack.getItem() == null ? 0xFFFFFF : stack.getItem().getColor(); }
}
