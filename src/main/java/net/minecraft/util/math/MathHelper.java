package net.minecraft.util.math;

public final class MathHelper {
    private MathHelper() { }

    public static int floor(double value) {
        int integer = (int) value;
        return value < integer ? integer - 1 : integer;
    }

    public static int floorDiv16(int value) {
        return Math.floorDiv(value, 16);
    }

    public static int floorMod16(int value) {
        return Math.floorMod(value, 16);
    }

    public static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    public static float sin(float value) { return (float) Math.sin(value); }
    public static float cos(float value) { return (float) Math.cos(value); }
    public static float sqrt(float value) { return (float) Math.sqrt(value); }
    public static int clampInt(int value, int min, int max) { return Math.max(min, Math.min(max, value)); }
}
