package net.minecraft.block;

public final class BlockDiamondOre extends BlockOre {
    public BlockDiamondOre(int id) { super(id, "diamond_ore", 15, 0x55D6D0, 3.0f); }
    @Override public net.minecraft.item.ItemStack[] getDrops(net.minecraft.world.World world, net.minecraft.util.math.BlockPos pos, int metadata, net.minecraft.item.ItemStack tool) {
        return new net.minecraft.item.ItemStack[] { new net.minecraft.item.ItemStack(net.minecraft.item.ItemRegistry.DIAMOND, 1) };
    }
}
