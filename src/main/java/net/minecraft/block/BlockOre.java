package net.minecraft.block;

import net.minecraft.block.material.Material;

public class BlockOre extends Block {
    private final int tint;
    public BlockOre(int id, String name, int texture, int tint, float hardness) {
        super(id, name, Material.ROCK, texture, true, true, false, hardness, 0);
        this.tint = tint;
    }
    @Override public int getRenderColor(int metadata) { return tint; }
}
