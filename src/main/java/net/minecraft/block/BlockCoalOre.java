package net.minecraft.block;

public final class BlockCoalOre extends BlockOre {
    public BlockCoalOre(int id) { super(id, "coal_ore", 12, 0x343434, 3.0f); }
    @Override public net.minecraft.item.ItemStack[] getDrops(net.minecraft.world.World world, net.minecraft.util.math.BlockPos pos, int metadata, net.minecraft.item.ItemStack tool) {
        return new net.minecraft.item.ItemStack[] { new net.minecraft.item.ItemStack(net.minecraft.item.ItemRegistry.COAL, 1 + world.getRandom().nextInt(2)) };
    }
}
