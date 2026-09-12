package net.minecraft.world.storage;

public final class WorldInfo {
    private final long seed;
    private long worldTime;
    private final String levelName;
    public WorldInfo(long seed, String levelName) { this.seed = seed; this.levelName = levelName; }
    public long getSeed() { return seed; }
    public long getWorldTime() { return worldTime; }
    public void setWorldTime(long worldTime) { this.worldTime = worldTime; }
    public String getLevelName() { return levelName; }
}
