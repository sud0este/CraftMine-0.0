package net.minecraft.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public final class ItemFood extends Item {
    private final int healAmount;
    public ItemFood(int id, String name, int healAmount) { super(id, name, 64, 0); this.healAmount = healAmount; }
    @Override public ItemStack onItemRightClick(ItemStack stack, EntityPlayer player, World world) {
        if (player.getHealth() < player.getMaxHealth()) {
            player.heal(healAmount);
            stack.shrink(1);
        }
        return stack;
    }
    @Override public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, net.minecraft.util.EnumFacing side) {
        onItemRightClick(stack, player, world);
        return true;
    }
}
