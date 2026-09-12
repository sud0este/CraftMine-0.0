package net.minecraft.item;

import net.minecraft.block.material.Material;

public final class ItemShovel extends ItemTool {
    public ItemShovel(int id, String name, float efficiency, int durability) {
        super(id, name, efficiency, Material.EARTH, durability);
    }
    @Override public boolean canHarvestBlock(ItemStack stack, net.minecraft.block.Block block) {
        return block.getMaterial() == Material.EARTH || block.getMaterial() == Material.SAND || super.canHarvestBlock(stack, block);
    }
}
