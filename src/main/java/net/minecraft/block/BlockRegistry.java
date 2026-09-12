package net.minecraft.block;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.block.material.Material;

/** Static registry deliberately kept simple, like the pre-registry-remap client. */
public final class BlockRegistry {
    private static final Map<Integer, Block> BY_ID = new LinkedHashMap<Integer, Block>();
    private static final Map<String, Block> BY_NAME = new LinkedHashMap<String, Block>();

    public static final Block AIR = register(new BlockAir(0));
    public static final Block STONE = register(new BlockStone(1));
    public static final Block GRASS = register(new BlockGrass(2));
    public static final Block DIRT = register(new BlockDirt(3));
    public static final Block COBBLESTONE = register(new BlockCobblestone(4));
    public static final Block PLANKS = register(new BlockPlanks(5));
    public static final Block LOG = register(new BlockLog(6));
    public static final Block LEAVES = register(new BlockLeaves(7));
    public static final Block SAND = register(new BlockSand(8));
    public static final Block GRAVEL = register(new BlockGravel(9));
    public static final Block COAL_ORE = register(new BlockCoalOre(10));
    public static final Block IRON_ORE = register(new BlockIronOre(11));
    public static final Block GOLD_ORE = register(new BlockGoldOre(12));
    public static final Block DIAMOND_ORE = register(new BlockDiamondOre(13));
    public static final Block GLASS = register(new BlockGlass(14));
    public static final Block WATER = register(new BlockWater(15));
    public static final Block BEDROCK = register(new BlockBedrock(16));
    public static final Block CRAFTING_TABLE = register(new BlockCraftingTable(17));
    public static final Block FURNACE = register(new BlockFurnace(18));
    public static final Block TORCH = register(new BlockTorch(19));

    private BlockRegistry() { }

    private static Block register(Block block) {
        if (BY_ID.put(block.getId(), block) != null) throw new IllegalStateException("Duplicate block id " + block.getId());
        if (BY_NAME.put(block.getName(), block) != null) throw new IllegalStateException("Duplicate block " + block.getName());
        return block;
    }

    public static Block get(int id) { return BY_ID.containsKey(id) ? BY_ID.get(id) : AIR; }
    public static Block get(String name) { return BY_NAME.get(name); }
    public static Map<Integer, Block> values() { return Collections.unmodifiableMap(BY_ID); }

    public static Block byMaterial(Material material) {
        for (Block block : BY_ID.values()) if (block.getMaterial() == material) return block;
        return AIR;
    }
}
