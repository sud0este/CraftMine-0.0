package net.minecraft.world.gen;

import java.util.Random;
import net.minecraft.block.BlockRegistry;
import net.minecraft.world.chunk.Chunk;

public final class WorldGenVillage implements StructureGenerator {
    @Override public void generate(Chunk chunk, long seed) {
        Random random = new Random(seed);
        if (random.nextInt(64) != 0) return;
        int x = 4 + random.nextInt(8), z = 4 + random.nextInt(8), y = chunk.getHeightValue(x, z);
        if (y < 4 || y + 5 >= Chunk.HEIGHT) return;
        for (int dx = -3; dx <= 3; dx++) for (int dz = -3; dz <= 3; dz++) {
            for (int dy = 0; dy < 4; dy++) {
                boolean wall = Math.abs(dx) == 3 || Math.abs(dz) == 3 || dy == 0;
                if (wall) chunk.setBlockState(x + dx, y + dy, z + dz, (dy == 0 ? BlockRegistry.COBBLESTONE : BlockRegistry.PLANKS).getDefaultState());
            }
        }
        for (int dx = -2; dx <= 2; dx++) for (int dz = -2; dz <= 2; dz++) chunk.setBlockState(x + dx, y + 4, z + dz, BlockRegistry.PLANKS.getDefaultState());
        chunk.setBlockState(x, y + 1, z - 3, BlockRegistry.AIR.getDefaultState());
    }
}
