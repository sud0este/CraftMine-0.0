package net.minecraft.block;

import net.minecraft.block.material.Material;

public final class BlockAir extends Block {
    public BlockAir(int id) { super(id, "air", Material.AIR, 0, false, false, true, 0.0f, 0); }
    @Override public boolean isAir() { return true; }
    @Override public boolean isReplaceable() { return true; }
}
