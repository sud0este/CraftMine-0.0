package net.minecraft.crafting;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public final class Ingredient {
    private final Item item;
    private final Block block;

    private Ingredient(Item item, Block block) { this.item = item; this.block = block; }
    public static Ingredient item(Item item) { return new Ingredient(item, null); }
    public static Ingredient block(Block block) { return new Ingredient(null, block); }
    public static Ingredient empty() { return new Ingredient(null, null); }

    public boolean matches(ItemStack stack) {
        if (item == null && block == null) return stack == null || stack.isEmpty();
        if (stack == null || stack.isEmpty()) return false;
        if (item != null) return stack.getItem() == item;
        return stack.getItem() instanceof net.minecraft.item.ItemBlock
                && ((net.minecraft.item.ItemBlock) stack.getItem()).getBlock() == block;
    }
}
