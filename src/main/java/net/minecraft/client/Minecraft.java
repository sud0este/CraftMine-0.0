package net.minecraft.client;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.gui.GuiManager;
import net.minecraft.client.input.Input;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.registry.RegistryBootstrap;
import net.minecraft.world.World;

/** Client shell and the fixed 20 TPS singleplayer loop. */
public final class Minecraft {
    private final long seed;
    private final GameSettings settings = new GameSettings();
    private Window window;
    private Input input;
    private World world;
    private EntityPlayer player;
    private WorldRenderer worldRenderer;
    private GuiManager gui;

    public Minecraft() { this(System.currentTimeMillis()); }
    public Minecraft(long seed) { this.seed = seed; }

    public void run() {
        RegistryBootstrap.init();
        window = new Window("CraftMine — 1.8.9 style", 1152, 648);
        window.create();
        input = new Input(window.getHandle());
        world = new World(seed);
        int spawnX = 0, spawnZ = 0, spawnY = world.getTopSolidOrLiquidBlock(0, 0);
        for (int radius = 0; radius < 64 && world.getBlockState(spawnX, spawnY, spawnZ).getBlock() == net.minecraft.block.BlockRegistry.WATER; radius++) {
            spawnX = radius;
            spawnZ = radius / 2;
            spawnY = world.getTopSolidOrLiquidBlock(spawnX, spawnZ);
        }
        player = new EntityPlayer(world, input, spawnX + 0.5, spawnY, spawnZ + 0.5);
        world.setPlayer(player);
        worldRenderer = new WorldRenderer(window, settings);
        gui = new GuiManager(window, input);

        long last = System.nanoTime();
        double accumulator = 0.0;
        final double tickLength = 1.0 / 20.0;
        try {
            while (!window.shouldClose()) {
                long now = System.nanoTime();
                double frame = Math.min(0.25, (now - last) / 1_000_000_000.0);
                last = now;
                accumulator += frame;
                input.poll();
                handleGlobalInput();
                while (accumulator >= tickLength) {
                    tick();
                    accumulator -= tickLength;
                }
                worldRenderer.render(world, player, (float) (accumulator / tickLength));
                gui.render(player);
                window.swapBuffers();
            }
        } finally {
            if (gui != null) gui.close();
            if (worldRenderer != null) worldRenderer.close();
            if (window != null) window.close();
        }
    }

    private void handleGlobalInput() {
        if (input.consumeKeyPress(GLFW.GLFW_KEY_ESCAPE)) {
            if (gui.isOpen()) gui.close(player);
            else input.setCursorCaptured(!input.isCursorCaptured());
        }
        if (input.consumeKeyPress(GLFW.GLFW_KEY_E)) {
            if (gui.isOpen()) gui.close(player); else gui.openInventory(player, false);
        }
        if (!gui.isOpen() && !input.isCursorCaptured() && input.consumeMousePress(GLFW.GLFW_MOUSE_BUTTON_LEFT)) input.setCursorCaptured(true);
    }

    private void tick() {
        if (gui.isOpen()) gui.tick(player);
        world.tick();
        if (player.consumeCraftingTableRequest()) gui.openCraftingTable(player);
    }
}
