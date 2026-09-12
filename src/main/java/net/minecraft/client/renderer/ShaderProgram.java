package net.minecraft.client.renderer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL20;

import net.minecraft.util.math.Matrix4f;

public final class ShaderProgram {
    private final int program;

    public ShaderProgram(String vertexSource, String fragmentSource) {
        int vertex = compile(GL20.GL_VERTEX_SHADER, vertexSource);
        int fragment = compile(GL20.GL_FRAGMENT_SHADER, fragmentSource);
        program = GL20.glCreateProgram();
        GL20.glAttachShader(program, vertex);
        GL20.glAttachShader(program, fragment);
        GL20.glLinkProgram(program);
        if (GL20.glGetProgrami(program, GL20.GL_LINK_STATUS) == GL20.GL_FALSE)
            throw new IllegalStateException("Shader link failed: " + GL20.glGetProgramInfoLog(program));
        GL20.glDeleteShader(vertex);
        GL20.glDeleteShader(fragment);
    }

    private static int compile(int type, String source) {
        int shader = GL20.glCreateShader(type);
        GL20.glShaderSource(shader, source);
        GL20.glCompileShader(shader);
        if (GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) == GL20.GL_FALSE)
            throw new IllegalStateException("Shader compile failed: " + GL20.glGetShaderInfoLog(shader));
        return shader;
    }

    public static String load(String path) {
        InputStream stream = ShaderProgram.class.getResourceAsStream(path);
        if (stream == null) throw new IllegalStateException("Missing shader resource " + path);
        StringBuilder result = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) result.append(line).append('\n');
        } catch (IOException exception) { throw new IllegalStateException("Cannot read shader " + path, exception); }
        return result.toString();
    }

    public void use() { GL20.glUseProgram(program); }
    public int getProgram() { return program; }
    public void setUniform1i(String name, int value) { GL20.glUniform1i(GL20.glGetUniformLocation(program, name), value); }
    public void setUniform1f(String name, float value) { GL20.glUniform1f(GL20.glGetUniformLocation(program, name), value); }
    public void setUniform4f(String name, float x, float y, float z, float w) { GL20.glUniform4f(GL20.glGetUniformLocation(program, name), x, y, z, w); }
    public void setMatrix(String name, Matrix4f matrix, FloatBuffer scratch) {
        matrix.store(scratch);
        GL20.glUniformMatrix4fv(GL20.glGetUniformLocation(program, name), false, scratch);
    }
    public void delete() { GL20.glDeleteProgram(program); }
}
