package net.minecraft.world.biome;

import net.minecraft.world.gen.OctaveNoise;

public final class BiomeProvider {
    private final OctaveNoise climate;
    public BiomeProvider(long seed) { climate = new OctaveNoise(seed ^ 0x6A09E667L, 3, 0.55); }

    public Biome getBiome(int x, int z) {
        double value = climate.noise(x * 0.003, z * 0.003);
        double dry = climate.noise((x + 10000) * 0.0015, (z - 10000) * 0.0015);
        if (value < -0.20) return Biome.OCEAN;
        if (dry < -0.25) return Biome.DESERT;
        if (value > 0.28) return Biome.MOUNTAINS;
        if (dry > 0.22) return Biome.FOREST;
        return Biome.PLAINS;
    }
}
