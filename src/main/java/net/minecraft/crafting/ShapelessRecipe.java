package net.minecraft.crafting;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

public final class ShapelessRecipe implements IRecipe {
    private final List<Ingredient> ingredients;
    private final ItemStack result;

    public ShapelessRecipe(ItemStack result, Ingredient... ingredients) {
        this.result = result;
        this.ingredients = new ArrayList<Ingredient>();
        for (Ingredient ingredient : ingredients) this.ingredients.add(ingredient);
    }

    @Override public boolean matches(ItemStack[] grid, int width, int height) {
        List<Ingredient> remaining = new ArrayList<Ingredient>(ingredients);
        for (ItemStack stack : grid) {
            if (stack == null || stack.isEmpty()) continue;
            int found = -1;
            for (int i = 0; i < remaining.size(); i++) if (remaining.get(i).matches(stack)) { found = i; break; }
            if (found < 0) return false;
            remaining.remove(found);
        }
        return remaining.isEmpty();
    }

    @Override public ItemStack getCraftingResult() { return result.copy(); }
    @Override public int getRecipeWidth() { return 0; }
    @Override public int getRecipeHeight() { return 0; }
}
