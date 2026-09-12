package net.minecraft.item;

import net.minecraft.block.material.Material;

public final class ItemAxe extends ItemTool {
    public ItemAxe(int id, String name, float efficiency, int durability) {
        super(id, name, efficiency, Material.WOOD, durability);
    }
}
