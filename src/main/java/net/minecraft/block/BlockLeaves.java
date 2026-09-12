package net.minecraft.block;

import net.minecraft.block.material.Material;

public class BlockLeaves extends Block {
    public BlockLeaves(int id) { super(id, "leaves", Material.LEAVES, 9, false, true, true, 0.2f, 0); }
    @Override public int getRenderColor(int metadata) { return 0x4F9B3A; }
    @Override public boolean isSolid() { return false; }
    @Override public net.minecraft.item.ItemStack[] getDrops(net.minecraft.world.World world, net.minecraft.util.math.BlockPos pos, int metadata, net.minecraft.item.ItemStack tool) {
        return world.getRandom().nextInt(12) == 0 ? new net.minecraft.item.ItemStack[] { new net.minecraft.item.ItemStack(net.minecraft.item.ItemRegistry.APPLE, 1) } : new net.minecraft.item.ItemStack[0];
    }
}
