package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class ItemTool extends Item {
    private final float efficiency;
    private final Material effectiveMaterial;

    public ItemTool(int id, String name, float efficiency, Material effectiveMaterial, int durability) {
        super(id, name, 1, durability);
        this.efficiency = efficiency;
        this.effectiveMaterial = effectiveMaterial;
    }

    @Override public float getDestroySpeed(ItemStack stack, Block block) {
        return block.getMaterial() == effectiveMaterial ? efficiency : 1.0f;
    }

    @Override public boolean canHarvestBlock(ItemStack stack, Block block) {
        return block.getMaterial() == effectiveMaterial || block.getHardness() <= 0.6f;
    }
}
