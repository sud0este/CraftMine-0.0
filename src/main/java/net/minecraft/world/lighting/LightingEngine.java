package net.minecraft.world.lighting;

import java.util.ArrayDeque;
import java.util.Queue;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

/**
 * Chunk-local light bootstrap plus a conservative flood fill for emitted block
 * light. Sky light is column based, which is the fast path used by terrain.
 */
public final class LightingEngine {
    private final World world;

    public LightingEngine(World world) { this.world = world; }

    public void initializeChunk(Chunk chunk) {
        for (int x = 0; x < Chunk.WIDTH; x++) for (int z = 0; z < Chunk.WIDTH; z++) {
            int sky = 15;
            for (int y = Chunk.HEIGHT - 1; y >= 0; y--) {
                Block block = chunk.getBlock(x, y, z);
                if (block.isOpaqueCube()) sky = 0;
                chunk.setSkyLight(x, y, z, sky);
                chunk.setBlockLight(x, y, z, block.getLightValue());
            }
        }
        floodBlockLight(chunk);
    }

    private void floodBlockLight(Chunk chunk) {
        Queue<int[]> queue = new ArrayDeque<int[]>();
        for (int x = 0; x < 16; x++) for (int y = 0; y < Chunk.HEIGHT; y++) for (int z = 0; z < 16; z++) {
            if (chunk.getBlockLight(x, y, z) > 0) queue.add(new int[] { x, y, z });
        }
        int[][] directions = { {1,0,0}, {-1,0,0}, {0,1,0}, {0,-1,0}, {0,0,1}, {0,0,-1} };
        while (!queue.isEmpty()) {
            int[] at = queue.remove();
            int light = chunk.getBlockLight(at[0], at[1], at[2]);
            if (light <= 1) continue;
            for (int[] direction : directions) {
                int x = at[0] + direction[0], y = at[1] + direction[1], z = at[2] + direction[2];
                if (x < 0 || x >= 16 || y < 0 || y >= Chunk.HEIGHT || z < 0 || z >= 16) continue;
                if (chunk.getBlock(x, y, z).isOpaqueCube()) continue;
                if (chunk.getBlockLight(x, y, z) + 1 < light) {
                    chunk.setBlockLight(x, y, z, light - 1);
                    queue.add(new int[] { x, y, z });
                }
            }
        }
    }

    public void updateAt(int x, int y, int z) {
        Chunk chunk = world.getChunkFromBlockCoords(x, z);
        initializeChunk(chunk);
    }
}
