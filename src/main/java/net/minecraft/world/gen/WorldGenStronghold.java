package net.minecraft.world.gen;

import net.minecraft.block.BlockRegistry;
import net.minecraft.world.chunk.Chunk;

/** A compact stronghold ring marker. Full portal logic can be layered on this structure API. */
public final class WorldGenStronghold implements StructureGenerator {
    @Override public void generate(Chunk chunk, long seed) {
        if ((chunk.getChunkX() * 341873128712L + chunk.getChunkZ() * 132897987541L + seed) % 257 != 0) return;
        int centerX = 8, centerZ = 8, y = 20;
        for (int x = 2; x < 15; x++) for (int z = 2; z < 15; z++) {
            int dx = x - centerX, dz = z - centerZ;
            if (dx * dx + dz * dz > 25 && dx * dx + dz * dz < 49)
                chunk.setBlockState(x, y, z, BlockRegistry.COBBLESTONE.getDefaultState());
        }
    }
}
