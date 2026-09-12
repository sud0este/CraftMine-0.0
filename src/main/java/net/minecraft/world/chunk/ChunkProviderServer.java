package net.minecraft.world.chunk;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGeneratorOverworld;

public final class ChunkProviderServer {
    private final World world;
    private final ChunkGeneratorOverworld generator;
    private final Map<Long, Chunk> loadedChunks = new LinkedHashMap<Long, Chunk>();

    public ChunkProviderServer(World world, long seed) {
        this.world = world;
        this.generator = new ChunkGeneratorOverworld(seed);
    }

    public Chunk provideChunk(int chunkX, int chunkZ) {
        long key = (((long) chunkX) << 32) ^ (chunkZ & 0xFFFFFFFFL);
        Chunk chunk = loadedChunks.get(key);
        if (chunk == null) {
            chunk = generator.generateChunk(chunkX, chunkZ);
            loadedChunks.put(key, chunk);
            world.getLightingEngine().initializeChunk(chunk);
        }
        return chunk;
    }

    public Collection<Chunk> getLoadedChunks() { return loadedChunks.values(); }
    public ChunkGeneratorOverworld getGenerator() { return generator; }
}
