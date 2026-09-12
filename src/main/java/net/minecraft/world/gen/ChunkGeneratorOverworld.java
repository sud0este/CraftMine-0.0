package net.minecraft.world.gen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRegistry;
import net.minecraft.block.state.BlockState;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.chunk.Chunk;

/** Deterministic overworld generator: layered terrain, caves, ores, trees and lightweight structures. */
public final class ChunkGeneratorOverworld {
    public static final int SEA_LEVEL = 62;
    private final long seed;
    private final BiomeProvider biomeProvider;
    private final OctaveNoise terrainNoise;
    private final OctaveNoise detailNoise;
    private final StructureGenerator[] structures;

    public ChunkGeneratorOverworld(long seed) {
        this.seed = seed;
        biomeProvider = new BiomeProvider(seed);
        terrainNoise = new OctaveNoise(seed ^ 0x1234ABCDL, 5, 0.5);
        detailNoise = new OctaveNoise(seed ^ 0x55AA55AAL, 3, 0.5);
        structures = new StructureGenerator[] { new WorldGenDungeons(), new WorldGenVillage(), new WorldGenMineshaft(), new WorldGenStronghold() };
    }

    public Chunk generateChunk(int chunkX, int chunkZ) {
        Chunk chunk = new Chunk(null, chunkX, chunkZ);
        Random random = new Random(seed + chunkX * 341873128712L + chunkZ * 132897987541L);
        int[] surfaces = new int[256];
        Biome[] biomes = new Biome[256];
        for (int localX = 0; localX < 16; localX++) for (int localZ = 0; localZ < 16; localZ++) {
            int worldX = chunkX * 16 + localX;
            int worldZ = chunkZ * 16 + localZ;
            Biome biome = biomeProvider.getBiome(worldX, worldZ);
            biomes[localZ * 16 + localX] = biome;
            double broad = terrainNoise.noise(worldX * 0.008, worldZ * 0.008);
            double detail = detailNoise.noise(worldX * 0.025, worldZ * 0.025);
            int surface = (int) (SEA_LEVEL + biome.getHeight() * 48.0 + broad * 23.0 + detail * 7.0);
            if (biome == Biome.OCEAN) surface = Math.min(surface, SEA_LEVEL - 4);
            surface = Math.max(4, Math.min(220, surface));
            surfaces[localZ * 16 + localX] = surface;
            for (int y = 0; y <= surface; y++) {
                Block block;
                if (y == 0) block = BlockRegistry.BEDROCK;
                else if (y < surface - 4) block = BlockRegistry.STONE;
                else if (y < surface) block = biome.getFillerBlock();
                else block = biome.getTopBlock();
                chunk.setBlockState(localX, y, localZ, block.getDefaultState());
            }
            for (int y = surface + 1; y <= SEA_LEVEL; y++) chunk.setBlockState(localX, y, localZ, BlockRegistry.WATER.getDefaultState());
        }

        carveCaves(chunk, random);
        generateOres(chunk, random);
        generateTrees(chunk, biomes, surfaces, random);
        for (StructureGenerator structure : structures) structure.generate(chunk, random.nextLong());
        chunk.setPopulated(true);
        return chunk;
    }

    private void carveCaves(Chunk chunk, Random random) {
        int caveCount = random.nextInt(5);
        for (int cave = 0; cave < caveCount; cave++) {
            double x = random.nextInt(16), y = 8 + random.nextInt(42), z = random.nextInt(16);
            double dx = random.nextDouble() * 2 - 1, dz = random.nextDouble() * 2 - 1;
            int length = 8 + random.nextInt(18);
            for (int step = 0; step < length; step++) {
                int radius = 1 + random.nextInt(2);
                for (int ox = -radius; ox <= radius; ox++) for (int oy = -radius; oy <= radius; oy++) for (int oz = -radius; oz <= radius; oz++) {
                    if (ox * ox + oy * oy + oz * oz > radius * radius) continue;
                    int bx = (int) x + ox, by = (int) y + oy, bz = (int) z + oz;
                    if (bx > 0 && bx < 15 && by > 2 && bz > 0 && bz < 15 && chunk.getBlock(bx, by, bz) == BlockRegistry.STONE)
                        chunk.setBlockState(bx, by, bz, BlockRegistry.AIR.getDefaultState());
                }
                x += dx; z += dz; y += random.nextDouble() * 0.8 - 0.4;
                if (x < 1 || x > 14 || z < 1 || z > 14 || y < 4 || y > 54) break;
            }
        }
    }

    private void generateOres(Chunk chunk, Random random) {
        vein(chunk, random, BlockRegistry.COAL_ORE, 22, 5, 64, 8);
        vein(chunk, random, BlockRegistry.IRON_ORE, 18, 5, 48, 6);
        vein(chunk, random, BlockRegistry.GOLD_ORE, 6, 5, 30, 5);
        vein(chunk, random, BlockRegistry.DIAMOND_ORE, 2, 5, 16, 4);
    }

    private void vein(Chunk chunk, Random random, Block ore, int attempts, int minY, int maxY, int size) {
        for (int attempt = 0; attempt < attempts; attempt++) {
            int x = 1 + random.nextInt(14), y = minY + random.nextInt(maxY - minY + 1), z = 1 + random.nextInt(14);
            for (int i = 0; i < size; i++) {
                int ox = Math.max(1, Math.min(14, x + random.nextInt(3) - 1));
                int oy = Math.max(minY, Math.min(maxY, y + random.nextInt(3) - 1));
                int oz = Math.max(1, Math.min(14, z + random.nextInt(3) - 1));
                if (chunk.getBlock(ox, oy, oz) == BlockRegistry.STONE) chunk.setBlockState(ox, oy, oz, ore.getDefaultState());
            }
        }
    }

    private void generateTrees(Chunk chunk, Biome[] biomes, int[] surfaces, Random random) {
        WorldGenTrees trees = new WorldGenTrees();
        for (int x = 2; x < 14; x++) for (int z = 2; z < 14; z++) {
            int index = z * 16 + x;
            Biome biome = biomes[index];
            if (biome.getTreeDensity() == 0 || random.nextInt(28 / biome.getTreeDensity()) != 0) continue;
            int ground = surfaces[index];
            if (ground > SEA_LEVEL && chunk.getBlock(x, ground, z) == BlockRegistry.GRASS) trees.generate(chunk, x, ground + 1, z, 4 + random.nextInt(3));
        }
    }

    public Biome getBiome(int x, int z) { return biomeProvider.getBiome(x, z); }
}
