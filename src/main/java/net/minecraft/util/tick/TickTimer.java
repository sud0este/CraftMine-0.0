package net.minecraft.util.tick;

public final class TickTimer {
    private final double tickSeconds;
    private double accumulator;
    public TickTimer(double ticksPerSecond) { tickSeconds = 1.0 / ticksPerSecond; }
    public int advance(double seconds) {
        accumulator += Math.min(seconds, 0.25);
        int ticks = 0;
        while (accumulator >= tickSeconds) { accumulator -= tickSeconds; ticks++; }
        return ticks;
    }
    public float getPartialTicks() { return (float) (accumulator / tickSeconds); }
}
