package net.minecraft.client.renderer;

import java.nio.FloatBuffer;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

/** Unit cube used for the deliberately simple, readable mob renderer. */
public final class CubeModel {
    private final int vao;
    private final int vbo;
    private final int vertexCount;

    public CubeModel() {
        float[] d = new float[36 * 9];
        int n = 0;
        float[][] faces = {
                {-0.5f,-0.5f,0.5f, 0.5f,-0.5f,0.5f, 0.5f,0.5f,0.5f, -0.5f,0.5f,0.5f},
                {0.5f,-0.5f,-0.5f, -0.5f,-0.5f,-0.5f, -0.5f,0.5f,-0.5f, 0.5f,0.5f,-0.5f},
                {-0.5f,0.5f,-0.5f, -0.5f,0.5f,0.5f, 0.5f,0.5f,0.5f, 0.5f,0.5f,-0.5f},
                {-0.5f,-0.5f,0.5f, 0.5f,-0.5f,0.5f, 0.5f,-0.5f,-0.5f, -0.5f,-0.5f,-0.5f},
                {0.5f,-0.5f,0.5f, 0.5f,-0.5f,-0.5f, 0.5f,0.5f,-0.5f, 0.5f,0.5f,0.5f},
                {-0.5f,-0.5f,-0.5f, -0.5f,0.5f,-0.5f, -0.5f,0.5f,0.5f, -0.5f,-0.5f,0.5f}
        };
        for (float[] face : faces) {
            int[] order = {0,1,2,0,2,3};
            for (int index : order) {
                d[n++] = face[index * 3]; d[n++] = face[index * 3 + 1]; d[n++] = face[index * 3 + 2];
                d[n++] = 0; d[n++] = 0; d[n++] = 1; d[n++] = 1; d[n++] = 1; d[n++] = 1;
            }
        }
        vertexCount = n / 9;
        vao = GL30.glGenVertexArrays(); vbo = GL15.glGenBuffers();
        FloatBuffer buffer = MemoryUtil.memAllocFloat(d.length); buffer.put(d).flip();
        GL30.glBindVertexArray(vao); GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo); GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(0, 3, GL20.GL_FLOAT, false, 36, 0);
        GL20.glVertexAttribPointer(1, 2, GL20.GL_FLOAT, false, 36, 12);
        GL20.glVertexAttribPointer(2, 3, GL20.GL_FLOAT, false, 36, 20);
        GL20.glVertexAttribPointer(3, 1, GL20.GL_FLOAT, false, 36, 32);
        GL20.glEnableVertexAttribArray(0); GL20.glEnableVertexAttribArray(1); GL20.glEnableVertexAttribArray(2); GL20.glEnableVertexAttribArray(3);
        GL30.glBindVertexArray(0); MemoryUtil.memFree(buffer);
    }
    public void render() { GL30.glBindVertexArray(vao); org.lwjgl.opengl.GL11.glDrawArrays(org.lwjgl.opengl.GL11.GL_TRIANGLES, 0, vertexCount); GL30.glBindVertexArray(0); }
    public void delete() { GL15.glDeleteBuffers(vbo); GL30.glDeleteVertexArrays(vao); }
}
