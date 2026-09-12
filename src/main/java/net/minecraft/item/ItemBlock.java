package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBlock extends Item {
    private final Block block;

    public ItemBlock(int id, Block block) {
        super(id, block.getName());
        this.block = block;
    }

    public Block getBlock() { return block; }

    @Override public int getColor() { return block.getRenderColor(0); }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world,
                              BlockPos clicked, EnumFacing side) {
        BlockPos target = clicked.offset(side);
        if (!world.isValidBuildHeight(target.y) || !block.canPlaceBlockAt(world, target)) return false;
        AxisAlignedBB box = block.getCollisionBoundingBox(world, target.x, target.y, target.z);
        if (box != null && player.getBoundingBox().intersects(box)) return false;
        if (world.setBlockState(target, block.getDefaultState())) {
            stack.shrink(1);
            return true;
        }
        return false;
    }
}
