package net.minecraft.world.chunk;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRegistry;
import net.minecraft.block.state.BlockState;
import net.minecraft.world.World;

/** 16x256x16 chunk with compact short ids, metadata and two nibble-like light arrays. */
public final class Chunk {
    public static final int WIDTH = 16;
    public static final int HEIGHT = 256;
    private final World world;
    private final int chunkX;
    private final int chunkZ;
    private final short[] blockIds = new short[WIDTH * HEIGHT * WIDTH];
    private final byte[] metadata = new byte[blockIds.length];
    private final byte[] skyLight = new byte[blockIds.length];
    private final byte[] blockLight = new byte[blockIds.length];
    private final int[] heightMap = new int[WIDTH * WIDTH];
    private int revision;
    private boolean populated;

    public Chunk(World world, int chunkX, int chunkZ) {
        this.world = world;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        for (int i = 0; i < heightMap.length; i++) heightMap[i] = 0;
    }

    private static int index(int localX, int y, int localZ) { return (y << 8) | (localZ << 4) | localX; }
    public int getChunkX() { return chunkX; }
    public int getChunkZ() { return chunkZ; }
    public int getRevision() { return revision; }
    public void markDirty() { revision++; }
    public boolean isPopulated() { return populated; }
    public void setPopulated(boolean populated) { this.populated = populated; }

    public BlockState getBlockState(int localX, int y, int localZ) {
        if (localX < 0 || localX >= WIDTH || localZ < 0 || localZ >= WIDTH || y < 0 || y >= HEIGHT) return BlockRegistry.AIR.getDefaultState();
        int index = index(localX, y, localZ);
        return new BlockState(BlockRegistry.get(blockIds[index] & 0xFFFF), metadata[index] & 15);
    }

    public Block getBlock(int localX, int y, int localZ) {
        if (y < 0 || y >= HEIGHT) return BlockRegistry.AIR;
        return BlockRegistry.get(blockIds[index(localX & 15, y, localZ & 15)] & 0xFFFF);
    }

    public void setBlockState(int localX, int y, int localZ, BlockState state) {
        if (localX < 0 || localX >= WIDTH || localZ < 0 || localZ >= WIDTH || y < 0 || y >= HEIGHT) return;
        int index = index(localX, y, localZ);
        blockIds[index] = (short) state.getBlock().getId();
        metadata[index] = (byte) state.getMetadata();
        if (state.getBlock().isSolid() && y + 1 > heightMap[localZ * 16 + localX]) heightMap[localZ * 16 + localX] = y + 1;
        revision++;
    }

    public int getHeightValue(int localX, int localZ) { return heightMap[(localZ & 15) * 16 + (localX & 15)]; }
    public void setHeightValue(int localX, int localZ, int value) { heightMap[(localZ & 15) * 16 + (localX & 15)] = value; }
    public int getSkyLight(int localX, int y, int localZ) { return skyLight[index(localX & 15, y, localZ & 15)] & 15; }
    public int getBlockLight(int localX, int y, int localZ) { return blockLight[index(localX & 15, y, localZ & 15)] & 15; }
    public void setSkyLight(int localX, int y, int localZ, int value) { skyLight[index(localX & 15, y, localZ & 15)] = (byte) (value & 15); }
    public void setBlockLight(int localX, int y, int localZ, int value) { blockLight[index(localX & 15, y, localZ & 15)] = (byte) (value & 15); }
}
