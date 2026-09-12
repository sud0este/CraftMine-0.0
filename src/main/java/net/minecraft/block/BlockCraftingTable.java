package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;

public class BlockCraftingTable extends Block {
    public BlockCraftingTable(int id) { super(id, "crafting_table", Material.WOOD, 19, true, true, false, 2.5f, 0); }
    @Override public int getTexture(EnumFacing face) { return face == EnumFacing.UP ? 20 : (face == EnumFacing.DOWN ? 6 : 19); }
    @Override public boolean onBlockActivated(World world, BlockPos pos, EntityPlayer player) { player.requestCraftingTable(); return true; }
}
