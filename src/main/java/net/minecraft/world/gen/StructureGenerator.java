package net.minecraft.world.gen;

import net.minecraft.world.chunk.Chunk;

public interface StructureGenerator {
    void generate(Chunk chunk, long seed);
}
