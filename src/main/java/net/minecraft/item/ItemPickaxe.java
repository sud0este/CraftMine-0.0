package net.minecraft.item;

import net.minecraft.block.material.Material;

public final class ItemPickaxe extends ItemTool {
    public ItemPickaxe(int id, String name, float efficiency, int durability) {
        super(id, name, efficiency, Material.ROCK, durability);
    }
}
