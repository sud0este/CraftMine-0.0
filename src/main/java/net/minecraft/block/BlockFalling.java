package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockFalling extends Block {
    public BlockFalling(int id, String name, Material material, int texture, float hardness) {
        super(id, name, material, texture, true, true, false, hardness, 0);
    }
    @Override public void onBlockAdded(World world, BlockPos pos) {
        if (pos.y > 0 && world.getBlockState(pos.x, pos.y - 1, pos.z).getBlock().isAir()) {
            world.setBlockState(pos, BlockRegistry.AIR.getDefaultState());
            world.spawnEntity(new EntityFallingBlock(world, pos.x + 0.5, pos.y, pos.z + 0.5, this));
        }
    }
}
