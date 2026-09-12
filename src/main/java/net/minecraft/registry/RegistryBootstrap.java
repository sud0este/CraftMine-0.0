package net.minecraft.registry;

import net.minecraft.block.BlockRegistry;
import net.minecraft.crafting.CraftingManager;
import net.minecraft.item.ItemRegistry;

/** One explicit bootstrap point keeps static registry order deterministic. */
public final class RegistryBootstrap {
    private RegistryBootstrap() { }
    public static void init() {
        BlockRegistry.get(0);
        ItemRegistry.init();
        CraftingManager.getInstance();
    }
}
