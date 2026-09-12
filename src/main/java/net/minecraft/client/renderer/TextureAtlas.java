package net.minecraft.client.renderer;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.system.MemoryUtil;

/**
 * Atlas contract used by block meshes. It deliberately generates placeholder
 * art rather than bundling Minecraft's copyrighted textures.
 */
public final class TextureAtlas {
    public static final int TILE_SIZE = 16;
    public static final int TILES_PER_ROW = 16;
    public static final int SIZE = TILE_SIZE * TILES_PER_ROW;
    private static final String[] TILE_NAMES = {
            "missing", "stone", "grass_side", "grass_top", "dirt", "cobblestone", "planks", "log", "log_top", "leaves",
            "sand", "gravel", "coal_ore", "iron_ore", "gold_ore", "diamond_ore", "glass", "water", "bedrock", "crafting_table",
            "crafting_table_top", "furnace", "torch"
    };
    private final int textureId;

    public TextureAtlas() {
        BufferedImage[] customTiles = loadCustomTiles();
        textureId = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
        ByteBuffer pixels = MemoryUtil.memAlloc(SIZE * SIZE * 4);
        for (int y = 0; y < SIZE; y++) for (int x = 0; x < SIZE; x++) {
            int tile = (y / TILE_SIZE) * TILES_PER_ROW + x / TILE_SIZE;
            int localX = x & (TILE_SIZE - 1), localY = y & (TILE_SIZE - 1);
            BufferedImage custom = tile < customTiles.length ? customTiles[tile] : null;
            if (custom != null) {
                int argb = custom.getRGB(localX * custom.getWidth() / TILE_SIZE, localY * custom.getHeight() / TILE_SIZE);
                pixels.put((byte) ((argb >> 16) & 255)).put((byte) ((argb >> 8) & 255)).put((byte) (argb & 255)).put((byte) ((argb >>> 24) & 255));
            } else {
                int[] color = colorFor(tile);
                int variation = ((localX * 13 + localY * 7 + tile * 3) & 15) - 7;
                int r = clamp(color[0] + variation), g = clamp(color[1] + variation), b = clamp(color[2] + variation);
                int alpha = color[3];
                if (tile == 5 && ((localX + localY) & 3) == 0) { r /= 2; g /= 2; b /= 2; }
                pixels.put((byte) r).put((byte) g).put((byte) b).put((byte) alpha);
            }
        }
        pixels.flip();
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, SIZE, SIZE, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixels);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        MemoryUtil.memFree(pixels);
    }

    private static BufferedImage[] loadCustomTiles() {
        BufferedImage[] tiles = new BufferedImage[TILE_NAMES.length];
        for (int tile = 0; tile < TILE_NAMES.length; tile++) {
            String path = "/assets/craftmine/textures/blocks/" + TILE_NAMES[tile] + ".png";
            try (InputStream stream = TextureAtlas.class.getResourceAsStream(path)) {
                if (stream != null) tiles[tile] = ImageIO.read(stream);
            } catch (IOException ignored) {
                // A missing or malformed optional texture falls back to the procedural tile.
            }
        }
        return tiles;
    }

    private static int[] colorFor(int tile) {
        switch (tile) {
            case 0: return new int[] {255, 255, 255, 255};
            case 1: return new int[] {130, 130, 136, 255};
            case 2: return new int[] {92, 140, 52, 255};
            case 3: return new int[] {95, 170, 55, 255};
            case 4: return new int[] {125, 83, 50, 255};
            case 5: return new int[] {110, 110, 110, 255};
            case 6: return new int[] {167, 116, 62, 255};
            case 7: return new int[] {111, 76, 43, 255};
            case 8: return new int[] {160, 107, 58, 255};
            case 9: return new int[] {65, 145, 53, 225};
            case 10: return new int[] {218, 193, 121, 255};
            case 11: return new int[] {135, 135, 132, 255};
            case 12: return new int[] {100, 100, 104, 255};
            case 13: return new int[] {187, 156, 125, 255};
            case 14: return new int[] {225, 180, 45, 255};
            case 15: return new int[] {55, 205, 204, 255};
            case 16: return new int[] {180, 230, 245, 100};
            case 17: return new int[] {45, 120, 210, 155};
            case 18: return new int[] {55, 55, 58, 255};
            case 19: return new int[] {173, 119, 66, 255};
            case 20: return new int[] {194, 143, 80, 255};
            case 21: return new int[] {100, 100, 100, 255};
            case 22: return new int[] {255, 180, 50, 255};
            default: return new int[] {190, 190, 190, 255};
        }
    }

    private static int clamp(int value) { return Math.max(0, Math.min(255, value)); }
    public int getTextureId() { return textureId; }
    public float u0(int tile) { return (tile % TILES_PER_ROW) / (float) TILES_PER_ROW + 0.001f; }
    public float u1(int tile) { return (tile % TILES_PER_ROW + 1) / (float) TILES_PER_ROW - 0.001f; }
    public float v0(int tile) { return (tile / TILES_PER_ROW) / (float) TILES_PER_ROW + 0.001f; }
    public float v1(int tile) { return (tile / TILES_PER_ROW + 1) / (float) TILES_PER_ROW - 0.001f; }
    public void bind() { GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId); }
    public void delete() { GL11.glDeleteTextures(textureId); }
}
