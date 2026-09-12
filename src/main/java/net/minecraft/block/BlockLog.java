package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;

public class BlockLog extends Block {
    public BlockLog(int id) { super(id, "log", Material.WOOD, 7, true, true, false, 2.0f, 0); }
    @Override public int getTexture(EnumFacing face) { return face == EnumFacing.UP || face == EnumFacing.DOWN ? 8 : 7; }
}
