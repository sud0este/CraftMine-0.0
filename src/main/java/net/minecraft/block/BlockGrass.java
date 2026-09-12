package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;

public class BlockGrass extends Block {
    public BlockGrass(int id) { super(id, "grass", Material.EARTH, 2, true, true, false, 0.6f, 0); }
    @Override public int getTexture(EnumFacing face) {
        return face == EnumFacing.UP ? 3 : (face == EnumFacing.DOWN ? 4 : 2);
    }
    @Override public int getRenderColor(int metadata) { return 0x70B83F; }
}
