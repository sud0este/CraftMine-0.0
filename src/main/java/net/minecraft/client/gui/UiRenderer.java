package net.minecraft.client.gui;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import net.minecraft.client.Window;
import net.minecraft.client.renderer.ShaderProgram;

/** Batched colored rectangles and a tiny bitmap font; no external font asset is required. */
public final class UiRenderer {
    private final Window window;
    private final ShaderProgram shader;
    private final int vao;
    private final int vbo;
    private final List<Float> vertices = new ArrayList<Float>();

    public UiRenderer(Window window) {
        this.window = window;
        shader = new ShaderProgram(ShaderProgram.load("/assets/craftmine/shaders/ui.vert"), ShaderProgram.load("/assets/craftmine/shaders/ui.frag"));
        vao = GL30.glGenVertexArrays(); vbo = GL15.glGenBuffers();
        GL30.glBindVertexArray(vao); GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL20.glVertexAttribPointer(0, 2, GL20.GL_FLOAT, false, 24, 0);
        GL20.glVertexAttribPointer(1, 4, GL20.GL_FLOAT, false, 24, 8);
        GL20.glEnableVertexAttribArray(0); GL20.glEnableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
    }

    public void begin() { vertices.clear(); }

    public void rect(float x, float y, float width, float height, int color, float alpha) {
        float x0 = toX(x), x1 = toX(x + width), y0 = toY(y), y1 = toY(y + height);
        vertex(x0, y0, color, alpha); vertex(x1, y0, color, alpha); vertex(x1, y1, color, alpha);
        vertex(x0, y0, color, alpha); vertex(x1, y1, color, alpha); vertex(x0, y1, color, alpha);
    }

    public void border(float x, float y, float width, float height, float thickness, int color, float alpha) {
        rect(x, y, width, thickness, color, alpha);
        rect(x, y + height - thickness, width, thickness, color, alpha);
        rect(x, y + thickness, thickness, height - thickness * 2, color, alpha);
        rect(x + width - thickness, y + thickness, thickness, height - thickness * 2, color, alpha);
    }

    public void text(String value, float x, float y, float scale, int color, float alpha) {
        float cursor = x;
        for (int i = 0; i < value.length(); i++) {
            char character = Character.toUpperCase(value.charAt(i));
            if (character == ' ') { cursor += 6 * scale; continue; }
            String[] glyph = glyph(character);
            for (int row = 0; row < glyph.length; row++) for (int column = 0; column < glyph[row].length(); column++)
                if (glyph[row].charAt(column) == '#') rect(cursor + column * scale, y + row * scale, scale, scale, color, alpha);
            cursor += 6 * scale;
        }
    }

    private void vertex(float x, float y, int color, float alpha) {
        vertices.add(x); vertices.add(y);
        vertices.add(((color >> 16) & 255) / 255.0f); vertices.add(((color >> 8) & 255) / 255.0f);
        vertices.add((color & 255) / 255.0f); vertices.add(alpha);
    }

    private float toX(float pixels) { return -1.0f + pixels / Math.max(1, window.getWidth()) * 2.0f; }
    private float toY(float pixels) { return 1.0f - pixels / Math.max(1, window.getHeight()) * 2.0f; }

    public void end() {
        if (vertices.isEmpty()) return;
        FloatBuffer buffer = MemoryUtil.memAllocFloat(vertices.size());
        for (Float value : vertices) buffer.put(value);
        buffer.flip();
        shader.use();
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_DYNAMIC_DRAW);
        GL30.glBindVertexArray(vao);
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vertices.size() / 6);
        GL30.glBindVertexArray(0);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        MemoryUtil.memFree(buffer);
    }

    private static String[] glyph(char c) {
        switch (c) {
            case 'A': return new String[]{".###.","#...#","#...#","#####","#...#","#...#","#...#"};
            case 'B': return new String[]{"####.","#...#","#...#","####.","#...#","#...#","####."};
            case 'C': return new String[]{".####","#....","#....","#....","#....","#....",".####"};
            case 'D': return new String[]{"####.","#...#","#...#","#...#","#...#","#...#","####."};
            case 'E': return new String[]{"#####","#....","#....","####.","#....","#....","#####"};
            case 'F': return new String[]{"#####","#....","#....","####.","#....","#....","#...."};
            case 'G': return new String[]{".####","#....","#....","#.###","#...#","#...#",".###."};
            case 'H': return new String[]{"#...#","#...#","#...#","#####","#...#","#...#","#...#"};
            case 'I': return new String[]{"#####","..#..","..#..","..#..","..#..","..#..","#####"};
            case 'J': return new String[]{"..###","...#.","...#.","...#.","#..#.","#..#.",".##.."};
            case 'K': return new String[]{"#...#","#..#.","#.#..","##...","#.#..","#..#.","#...#"};
            case 'L': return new String[]{"#....","#....","#....","#....","#....","#....","#####"};
            case 'M': return new String[]{"#...#","##.##","#.#.#","#.#.#","#...#","#...#","#...#"};
            case 'N': return new String[]{"#...#","##..#","##..#","#.#.#","#..##","#..##","#...#"};
            case 'O': return new String[]{".###.","#...#","#...#","#...#","#...#","#...#",".###."};
            case 'P': return new String[]{"####.","#...#","#...#","####.","#....","#....","#...."};
            case 'Q': return new String[]{".###.","#...#","#...#","#...#","#.#.#","#..#.",".##.#"};
            case 'R': return new String[]{"####.","#...#","#...#","####.","#.#..","#..#.","#...#"};
            case 'S': return new String[]{".####","#....","#....",".###.","....#","....#","####."};
            case 'T': return new String[]{"#####","..#..","..#..","..#..","..#..","..#..","..#.."};
            case 'U': return new String[]{"#...#","#...#","#...#","#...#","#...#","#...#",".###."};
            case 'V': return new String[]{"#...#","#...#","#...#","#...#","#...#",".#.#.","..#.."};
            case 'W': return new String[]{"#...#","#...#","#...#","#.#.#","#.#.#","##.##","#...#"};
            case 'X': return new String[]{"#...#","#...#",".#.#.","..#..",".#.#.","#...#","#...#"};
            case 'Y': return new String[]{"#...#","#...#",".#.#.","..#..","..#..","..#..","..#.."};
            case 'Z': return new String[]{"#####","....#","...#.","..#..",".#...","#....","#####"};
            case '0': return new String[]{".###.","#...#","#..##","#.#.#","##..#","#...#",".###."};
            case '1': return new String[]{"..#..",".##..","..#..","..#..","..#..","..#..",".###."};
            case '2': return new String[]{".###.","#...#","....#","...#.","..#..",".#...","#####"};
            case '3': return new String[]{"####.","....#","....#",".###.","....#","....#","####."};
            case '4': return new String[]{"...#.","..##.",".#.#.","#..#.","#####","...#.","...#."};
            case '5': return new String[]{"#####","#....","#....","####.","....#","....#","####."};
            case '6': return new String[]{".###.","#....","#....","####.","#...#","#...#",".###."};
            case '7': return new String[]{"#####","....#","...#.","..#..",".#...",".#...",".#..."};
            case '8': return new String[]{".###.","#...#","#...#",".###.","#...#","#...#",".###."};
            case '9': return new String[]{".###.","#...#","#...#",".####","....#","....#",".###."};
            case ':': return new String[]{".....","..#..",".....",".....","..#..",".....","....."};
            case '/': return new String[]{"....#","...#.","...#.","..#..",".#...",".#...","#...."};
            default: return new String[]{"#####","#...#","..#..","..#..","..#..","#...#","#####"};
        }
    }

    public void close() { shader.delete(); GL15.glDeleteBuffers(vbo); GL30.glDeleteVertexArrays(vao); }
}
