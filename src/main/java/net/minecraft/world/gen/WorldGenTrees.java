package net.minecraft.world.gen;

import net.minecraft.block.BlockRegistry;
import net.minecraft.world.chunk.Chunk;

public final class WorldGenTrees {
    public boolean generate(Chunk chunk, int localX, int groundY, int localZ, int height) {
        if (localX < 2 || localX > 13 || localZ < 2 || localZ > 13 || groundY < 1 || groundY + height + 2 >= Chunk.HEIGHT) return false;
        for (int y = 0; y <= height + 1; y++) {
            int radius = y < height - 2 ? 0 : (y == height || y == height + 1 ? 2 : 1);
            for (int dx = -radius; dx <= radius; dx++) for (int dz = -radius; dz <= radius; dz++) {
                if (Math.abs(dx) == radius && Math.abs(dz) == radius && radius > 1) continue;
                int yy = groundY + y;
                if (chunk.getBlock(localX + dx, yy, localZ + dz).isAir() || chunk.getBlock(localX + dx, yy, localZ + dz) == BlockRegistry.LEAVES)
                    chunk.setBlockState(localX + dx, yy, localZ + dz, BlockRegistry.LEAVES.getDefaultState());
            }
        }
        for (int y = 0; y < height; y++) chunk.setBlockState(localX, groundY + y, localZ, BlockRegistry.LOG.getDefaultState());
        return true;
    }
}
