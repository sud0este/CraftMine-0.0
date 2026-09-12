package net.minecraft.crafting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.block.BlockRegistry;
import net.minecraft.item.ItemRegistry;
import net.minecraft.item.ItemStack;

/** Recipe book for both the player 2x2 grid and the 3x3 table grid. */
public final class CraftingManager {
    private static final CraftingManager INSTANCE = new CraftingManager();
    private final List<IRecipe> recipes = new ArrayList<IRecipe>();

    private CraftingManager() {
        add(new ShapedRecipe(1, 1, new Ingredient[] { Ingredient.item(ItemRegistry.LOG) }, new ItemStack(ItemRegistry.PLANKS, 4)));
        add(new ShapedRecipe(1, 2, new Ingredient[] { Ingredient.item(ItemRegistry.PLANKS), Ingredient.item(ItemRegistry.PLANKS) }, new ItemStack(ItemRegistry.STICK, 4)));
        add(new ShapedRecipe(2, 2, new Ingredient[] {
                Ingredient.item(ItemRegistry.PLANKS), Ingredient.item(ItemRegistry.PLANKS),
                Ingredient.item(ItemRegistry.PLANKS), Ingredient.item(ItemRegistry.PLANKS)
        }, new ItemStack(ItemRegistry.CRAFTING_TABLE, 1)));
        add(new ShapedRecipe(3, 3, new Ingredient[] {
                Ingredient.item(ItemRegistry.PLANKS), Ingredient.item(ItemRegistry.PLANKS), Ingredient.item(ItemRegistry.PLANKS),
                Ingredient.empty(), Ingredient.item(ItemRegistry.STICK), Ingredient.empty(),
                Ingredient.empty(), Ingredient.item(ItemRegistry.STICK), Ingredient.empty()
        }, new ItemStack(ItemRegistry.WOODEN_PICKAXE, 1)));
        add(new ShapedRecipe(3, 3, new Ingredient[] {
                Ingredient.item(ItemRegistry.COBBLESTONE), Ingredient.item(ItemRegistry.COBBLESTONE), Ingredient.item(ItemRegistry.COBBLESTONE),
                Ingredient.empty(), Ingredient.item(ItemRegistry.STICK), Ingredient.empty(),
                Ingredient.empty(), Ingredient.item(ItemRegistry.STICK), Ingredient.empty()
        }, new ItemStack(ItemRegistry.STONE_PICKAXE, 1)));
        add(new ShapelessRecipe(new ItemStack(ItemRegistry.BREAD, 1),
                Ingredient.item(ItemRegistry.WHEAT), Ingredient.item(ItemRegistry.WHEAT), Ingredient.item(ItemRegistry.WHEAT)));
        add(new ShapelessRecipe(new ItemStack(ItemRegistry.TORCH, 4),
                Ingredient.item(ItemRegistry.COAL), Ingredient.item(ItemRegistry.STICK)));
    }

    public static CraftingManager getInstance() { return INSTANCE; }
    public void add(IRecipe recipe) { recipes.add(recipe); }
    public List<IRecipe> getRecipes() { return Collections.unmodifiableList(recipes); }

    public ItemStack findMatchingRecipe(ItemStack[] grid, int width, int height) {
        for (IRecipe recipe : recipes) if (recipe.matches(grid, width, height)) return recipe.getCraftingResult();
        return null;
    }

    public boolean consumeIngredients(ItemStack[] grid, int width, int height) {
        IRecipe found = null;
        for (IRecipe recipe : recipes) if (recipe.matches(grid, width, height)) { found = recipe; break; }
        if (found == null) return false;
        for (int i = 0; i < grid.length; i++) {
            if (grid[i] != null && !grid[i].isEmpty()) grid[i].shrink(1);
        }
        return true;
    }
}
