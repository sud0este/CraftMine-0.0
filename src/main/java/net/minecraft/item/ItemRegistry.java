package net.minecraft.item;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRegistry;

public final class ItemRegistry {
    private static final Map<Integer, Item> BY_ID = new LinkedHashMap<Integer, Item>();
    private static final Map<String, Item> BY_NAME = new LinkedHashMap<String, Item>();
    private static final Map<Block, ItemBlock> BLOCK_ITEMS = new LinkedHashMap<Block, ItemBlock>();

    public static final Item STONE = block(100, BlockRegistry.STONE);
    public static final Item GRASS = block(101, BlockRegistry.GRASS);
    public static final Item DIRT = block(102, BlockRegistry.DIRT);
    public static final Item COBBLESTONE = block(103, BlockRegistry.COBBLESTONE);
    public static final Item PLANKS = block(104, BlockRegistry.PLANKS);
    public static final Item LOG = block(105, BlockRegistry.LOG);
    public static final Item LEAVES = block(106, BlockRegistry.LEAVES);
    public static final Item SAND = block(107, BlockRegistry.SAND);
    public static final Item GRAVEL = block(108, BlockRegistry.GRAVEL);
    public static final Item COAL_ORE = block(109, BlockRegistry.COAL_ORE);
    public static final Item IRON_ORE = block(110, BlockRegistry.IRON_ORE);
    public static final Item GOLD_ORE = block(111, BlockRegistry.GOLD_ORE);
    public static final Item DIAMOND_ORE = block(112, BlockRegistry.DIAMOND_ORE);
    public static final Item GLASS = block(113, BlockRegistry.GLASS);
    public static final Item WATER = block(114, BlockRegistry.WATER);
    public static final Item BEDROCK = block(115, BlockRegistry.BEDROCK);
    public static final Item CRAFTING_TABLE = block(116, BlockRegistry.CRAFTING_TABLE);
    public static final Item FURNACE = block(117, BlockRegistry.FURNACE);
    public static final Item TORCH = block(118, BlockRegistry.TORCH);

    public static final Item STICK = register(new Item(200, "stick"));
    public static final Item COAL = register(new Item(201, "coal"));
    public static final Item IRON_INGOT = register(new Item(202, "iron_ingot"));
    public static final Item GOLD_INGOT = register(new Item(203, "gold_ingot"));
    public static final Item DIAMOND = register(new Item(204, "diamond"));
    public static final Item WHEAT = register(new Item(205, "wheat"));
    public static final Item BREAD = register(new ItemFood(206, "bread", 5));
    public static final Item APPLE = register(new ItemFood(207, "apple", 4));
    public static final Item RAW_BEEF = register(new Item(208, "raw_beef"));
    public static final Item PORKCHOP = register(new Item(209, "porkchop"));
    public static final Item WOOL = register(new Item(210, "wool"));
    public static final Item FEATHER = register(new Item(211, "feather"));

    public static final Item WOODEN_PICKAXE = register(new ItemPickaxe(300, "wooden_pickaxe", 2.0f, 59));
    public static final Item STONE_PICKAXE = register(new ItemPickaxe(301, "stone_pickaxe", 4.0f, 131));
    public static final Item IRON_PICKAXE = register(new ItemPickaxe(302, "iron_pickaxe", 6.0f, 250));
    public static final Item DIAMOND_PICKAXE = register(new ItemPickaxe(303, "diamond_pickaxe", 8.0f, 1561));
    public static final Item WOODEN_AXE = register(new ItemAxe(304, "wooden_axe", 2.0f, 59));
    public static final Item STONE_AXE = register(new ItemAxe(305, "stone_axe", 4.0f, 131));
    public static final Item WOODEN_SHOVEL = register(new ItemShovel(306, "wooden_shovel", 2.0f, 59));
    public static final Item STONE_SHOVEL = register(new ItemShovel(307, "stone_shovel", 4.0f, 131));
    public static final Item WOODEN_SWORD = register(new ItemSword(308, "wooden_sword", 4.0f, 59));
    public static final Item STONE_SWORD = register(new ItemSword(309, "stone_sword", 5.0f, 131));
    public static final Item IRON_SWORD = register(new ItemSword(310, "iron_sword", 6.0f, 250));
    public static final Item DIAMOND_SWORD = register(new ItemSword(311, "diamond_sword", 7.0f, 1561));
    public static final Item LEATHER_HELMET = register(new ItemArmor(320, "leather_helmet", ItemArmor.ArmorSlot.HELMET, 1, 55));
    public static final Item LEATHER_CHESTPLATE = register(new ItemArmor(321, "leather_chestplate", ItemArmor.ArmorSlot.CHESTPLATE, 3, 80));
    public static final Item LEATHER_LEGGINGS = register(new ItemArmor(322, "leather_leggings", ItemArmor.ArmorSlot.LEGGINGS, 2, 75));
    public static final Item LEATHER_BOOTS = register(new ItemArmor(323, "leather_boots", ItemArmor.ArmorSlot.BOOTS, 1, 65));

    private ItemRegistry() { }

    private static ItemBlock block(int id, Block block) {
        ItemBlock item = new ItemBlock(id, block);
        register(item);
        BLOCK_ITEMS.put(block, item);
        return item;
    }

    private static <T extends Item> T register(T item) {
        if (BY_ID.put(item.getId(), item) != null) throw new IllegalStateException("Duplicate item id " + item.getId());
        if (BY_NAME.put(item.getName(), item) != null) throw new IllegalStateException("Duplicate item " + item.getName());
        return item;
    }

    public static Item get(int id) { return BY_ID.get(id); }
    public static Item get(String name) { return BY_NAME.get(name); }
    public static Item getBlockItem(Block block) { return BLOCK_ITEMS.get(block); }
    public static Map<Integer, Item> values() { return Collections.unmodifiableMap(BY_ID); }
    public static void init() { /* touching this method forces class initialization */ }
}
