package net.minecraft.world.gen;

import java.util.Random;

/** Compact 2D improved Perlin noise with a deterministic permutation table. */
public final class PerlinNoise implements NoiseGenerator {
    private final int[] permutation = new int[512];

    public PerlinNoise(long seed) {
        int[] source = new int[256];
        for (int i = 0; i < source.length; i++) source[i] = i;
        Random random = new Random(seed);
        for (int i = 255; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = source[i]; source[i] = source[j]; source[j] = temp;
        }
        for (int i = 0; i < permutation.length; i++) permutation[i] = source[i & 255];
    }

    @Override public double noise(double x, double z) {
        int floorX = fastFloor(x) & 255;
        int floorZ = fastFloor(z) & 255;
        double localX = x - fastFloor(x);
        double localZ = z - fastFloor(z);
        double u = fade(localX);
        double v = fade(localZ);
        int a = permutation[floorX] + floorZ;
        int b = permutation[floorX + 1] + floorZ;
        return lerp(v,
                lerp(u, grad(permutation[a], localX, localZ), grad(permutation[b], localX - 1, localZ)),
                lerp(u, grad(permutation[a + 1], localX, localZ - 1), grad(permutation[b + 1], localX - 1, localZ - 1)));
    }

    private static int fastFloor(double value) { return value >= 0 ? (int) value : (int) value - 1; }
    private static double fade(double value) { return value * value * value * (value * (value * 6 - 15) + 10); }
    private static double lerp(double alpha, double a, double b) { return a + alpha * (b - a); }
    private static double grad(int hash, double x, double z) {
        switch (hash & 3) {
            case 0: return x + z;
            case 1: return -x + z;
            case 2: return x - z;
            default: return -x - z;
        }
    }
}
