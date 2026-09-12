package net.minecraft.world.gen;

public final class OctaveNoise implements NoiseGenerator {
    private final PerlinNoise[] octaves;
    private final double persistence;

    public OctaveNoise(long seed, int octaveCount, double persistence) {
        this.persistence = persistence;
        octaves = new PerlinNoise[octaveCount];
        for (int i = 0; i < octaveCount; i++) octaves[i] = new PerlinNoise(seed + i * 341873128712L);
    }

    @Override public double noise(double x, double z) {
        double amplitude = 1.0;
        double frequency = 1.0;
        double total = 0.0;
        double normalization = 0.0;
        for (PerlinNoise octave : octaves) {
            total += octave.noise(x * frequency, z * frequency) * amplitude;
            normalization += amplitude;
            amplitude *= persistence;
            frequency *= 2.0;
        }
        return normalization == 0 ? 0 : total / normalization;
    }
}
