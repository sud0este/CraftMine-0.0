package net.minecraft.crafting;

import net.minecraft.item.ItemStack;

public interface IRecipe {
    boolean matches(ItemStack[] grid, int width, int height);
    ItemStack getCraftingResult();
    int getRecipeWidth();
    int getRecipeHeight();
}
