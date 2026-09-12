package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockFurnace extends Block {
    public BlockFurnace(int id) { super(id, "furnace", Material.ROCK, 21, true, true, false, 3.5f, 0); }
    @Override public boolean onBlockActivated(World world, BlockPos pos, EntityPlayer player) { player.requestCraftingTable(); return true; }
}
