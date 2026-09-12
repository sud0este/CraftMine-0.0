package net.minecraft.world.gen;

import java.util.Random;

import net.minecraft.block.BlockRegistry;
import net.minecraft.world.chunk.Chunk;

/** Small underground room; intentionally data-only, with no proprietary loot tables. */
public final class WorldGenDungeons implements StructureGenerator {
    @Override public void generate(Chunk chunk, long seed) {
        Random random = new Random(seed ^ 0xD00DCAFE1234L);
        if (random.nextInt(36) != 0) return;
        int centerX = 3 + random.nextInt(10);
        int centerZ = 3 + random.nextInt(10);
        int centerY = 12 + random.nextInt(22);
        for (int x = centerX - 2; x <= centerX + 2; x++) for (int z = centerZ - 2; z <= centerZ + 2; z++) {
            for (int y = centerY - 1; y <= centerY + 2; y++) {
                boolean wall = x == centerX - 2 || x == centerX + 2 || z == centerZ - 2 || z == centerZ + 2 || y == centerY - 1 || y == centerY + 2;
                if (wall) chunk.setBlockState(x, y, z, BlockRegistry.COBBLESTONE.getDefaultState());
                else chunk.setBlockState(x, y, z, BlockRegistry.AIR.getDefaultState());
            }
        }
    }
}
