package net.minecraft.crafting;

import net.minecraft.item.ItemStack;

public final class ShapedRecipe implements IRecipe {
    private final int width;
    private final int height;
    private final Ingredient[] ingredients;
    private final ItemStack result;

    public ShapedRecipe(int width, int height, Ingredient[] ingredients, ItemStack result) {
        if (ingredients.length != width * height) throw new IllegalArgumentException("Wrong shaped recipe size");
        this.width = width;
        this.height = height;
        this.ingredients = ingredients.clone();
        this.result = result;
    }

    @Override public boolean matches(ItemStack[] grid, int gridWidth, int gridHeight) {
        if (gridWidth < width || gridHeight < height) return false;
        for (int offsetY = 0; offsetY <= gridHeight - height; offsetY++) {
            for (int offsetX = 0; offsetX <= gridWidth - width; offsetX++) {
                if (matchesAt(grid, gridWidth, gridHeight, offsetX, offsetY, false)) return true;
                if (matchesAt(grid, gridWidth, gridHeight, offsetX, offsetY, true)) return true;
            }
        }
        return false;
    }

    private boolean matchesAt(ItemStack[] grid, int gridWidth, int gridHeight, int offsetX, int offsetY, boolean mirrored) {
        for (int y = 0; y < gridHeight; y++) {
            for (int x = 0; x < gridWidth; x++) {
                Ingredient expected = Ingredient.empty();
                if (x >= offsetX && x < offsetX + width && y >= offsetY && y < offsetY + height) {
                    int recipeX = x - offsetX;
                    int recipeY = y - offsetY;
                    if (mirrored) recipeX = width - recipeX - 1;
                    expected = ingredients[recipeY * width + recipeX];
                }
                ItemStack actual = y * gridWidth + x < grid.length ? grid[y * gridWidth + x] : null;
                if (!expected.matches(actual)) return false;
            }
        }
        return true;
    }

    @Override public ItemStack getCraftingResult() { return result.copy(); }
    @Override public int getRecipeWidth() { return width; }
    @Override public int getRecipeHeight() { return height; }
}
