package net.minecraft.server;

import net.minecraft.util.tick.ITickable;
import net.minecraft.world.WorldServer;

/** Minimal singleplayer server façade; the client and this world share one process. */
public final class MinecraftServer implements ITickable {
    private final WorldServer world;
    public MinecraftServer(long seed) { world = new WorldServer(seed); }
    public WorldServer getWorld() { return world; }
    @Override public void tick() { world.tick(); }
}
