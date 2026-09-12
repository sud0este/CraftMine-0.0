package net.minecraft.world.biome;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRegistry;

public class Biome {
    public static final Biome PLAINS = new Biome("Plains", 0.10f, 0.05f, BlockRegistry.GRASS, BlockRegistry.DIRT, 1);
    public static final Biome FOREST = new Biome("Forest", 0.18f, 0.08f, BlockRegistry.GRASS, BlockRegistry.DIRT, 2);
    public static final Biome DESERT = new Biome("Desert", 0.03f, 0.0f, BlockRegistry.SAND, BlockRegistry.SAND, 1);
    public static final Biome MOUNTAINS = new Biome("Mountains", 0.45f, 0.2f, BlockRegistry.GRASS, BlockRegistry.STONE, 3);
    public static final Biome OCEAN = new Biome("Ocean", -0.18f, 0.0f, BlockRegistry.SAND, BlockRegistry.SAND, 0);

    private final String name;
    private final float height;
    private final float scale;
    private final Block topBlock;
    private final Block fillerBlock;
    private final int treeDensity;

    public Biome(String name, float height, float scale, Block topBlock, Block fillerBlock, int treeDensity) {
        this.name = name;
        this.height = height;
        this.scale = scale;
        this.topBlock = topBlock;
        this.fillerBlock = fillerBlock;
        this.treeDensity = treeDensity;
    }

    public String getName() { return name; }
    public float getHeight() { return height; }
    public float getScale() { return scale; }
    public Block getTopBlock() { return topBlock; }
    public Block getFillerBlock() { return fillerBlock; }
    public int getTreeDensity() { return treeDensity; }
    public boolean isCold() { return this == MOUNTAINS; }
}
