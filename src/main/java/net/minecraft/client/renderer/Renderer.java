package net.minecraft.client.renderer;

import net.minecraft.client.Window;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/** Named façade kept separate from WorldRenderer for callers that expect a renderer service. */
public final class Renderer {
    private final WorldRenderer delegate;
    public Renderer(Window window, GameSettings settings) { delegate = new WorldRenderer(window, settings); }
    public void render(World world, EntityPlayer player, float partialTicks) { delegate.render(world, player, partialTicks); }
    public void close() { delegate.close(); }
}
