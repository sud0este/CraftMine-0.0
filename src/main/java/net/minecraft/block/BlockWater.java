package net.minecraft.block;

import net.minecraft.block.material.Material;

public class BlockWater extends Block {
    public BlockWater(int id) { super(id, "water", Material.WATER, 17, false, false, true, 100.0f, 0); }
    @Override public boolean isReplaceable() { return true; }
}
