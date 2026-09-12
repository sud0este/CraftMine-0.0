package net.minecraft.world.gen;

import java.util.Random;
import net.minecraft.block.BlockRegistry;
import net.minecraft.world.chunk.Chunk;

public final class WorldGenMineshaft implements StructureGenerator {
    @Override public void generate(Chunk chunk, long seed) {
        Random random = new Random(seed);
        if (random.nextInt(48) != 0) return;
        int y = 18 + random.nextInt(18);
        for (int x = 1; x < 15; x++) {
            for (int z = 1; z < 15; z++) {
                if (z == 1 || z == 14) {
                    chunk.setBlockState(x, y, z, BlockRegistry.LOG.getDefaultState());
                    chunk.setBlockState(x, y + 1, z, BlockRegistry.LOG.getDefaultState());
                    chunk.setBlockState(x, y + 2, z, BlockRegistry.LOG.getDefaultState());
                }
                if (x % 4 == 0) {
                    chunk.setBlockState(x, y, z, BlockRegistry.PLANKS.getDefaultState());
                    chunk.setBlockState(x, y + 1, z, BlockRegistry.PLANKS.getDefaultState());
                }
            }
        }
    }
}
