package net.minecraft.client.renderer;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public final class ChunkMesh {
    private final int vao;
    private final int vbo;
    private int vertexCount;
    private int builtRevision = Integer.MIN_VALUE;
    private final MeshBuilder builder;

    public ChunkMesh(TextureAtlas atlas) {
        builder = new MeshBuilder(atlas);
        vao = GL30.glGenVertexArrays();
        vbo = GL15.glGenBuffers();
        GL30.glBindVertexArray(vao);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL20.glVertexAttribPointer(0, 3, GL20.GL_FLOAT, false, 9 * 4, 0);
        GL20.glVertexAttribPointer(1, 2, GL20.GL_FLOAT, false, 9 * 4, 3 * 4);
        GL20.glVertexAttribPointer(2, 3, GL20.GL_FLOAT, false, 9 * 4, 5 * 4);
        GL20.glVertexAttribPointer(3, 1, GL20.GL_FLOAT, false, 9 * 4, 8 * 4);
        GL20.glEnableVertexAttribArray(0); GL20.glEnableVertexAttribArray(1); GL20.glEnableVertexAttribArray(2); GL20.glEnableVertexAttribArray(3);
        GL30.glBindVertexArray(0);
    }

    public void rebuild(World world, Chunk chunk) {
        if (builtRevision == chunk.getRevision()) return;
        builder.build(world, chunk);
        float[] data = builder.getData();
        FloatBuffer buffer = MemoryUtil.memAllocFloat(data.length);
        buffer.put(data).flip();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        MemoryUtil.memFree(buffer);
        vertexCount = builder.getVertexCount();
        builtRevision = chunk.getRevision();
    }

    public void render() {
        if (vertexCount == 0) return;
        GL30.glBindVertexArray(vao);
        GL11Compat.drawTriangles(vertexCount);
        GL30.glBindVertexArray(0);
    }

    public int getBuiltRevision() { return builtRevision; }
    public void delete() { GL15.glDeleteBuffers(vbo); GL30.glDeleteVertexArrays(vao); }

    private static final class GL11Compat {
        static void drawTriangles(int count) { org.lwjgl.opengl.GL11.glDrawArrays(org.lwjgl.opengl.GL11.GL_TRIANGLES, 0, count); }
    }
}
