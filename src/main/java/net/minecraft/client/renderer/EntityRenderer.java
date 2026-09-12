package net.minecraft.client.renderer;

import java.nio.FloatBuffer;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3;

public final class EntityRenderer {
    private final CubeModel cube;
    private final ShaderProgram shader;
    private final FloatBuffer matrixScratch;

    public EntityRenderer(CubeModel cube, ShaderProgram shader, FloatBuffer matrixScratch) {
        this.cube = cube; this.shader = shader; this.matrixScratch = matrixScratch;
    }

    public void render(Entity entity, Matrix4f viewProjection, Vec3 camera, float partialTicks) {
        double x = entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks - camera.x;
        double y = entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks - camera.y;
        double z = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks - camera.z;
        Matrix4f model = new Matrix4f().translate((float) x, (float) (y + entity.getHeight() * 0.5), (float) z)
                .scale(entity.getWidth(), entity.getHeight(), entity.getWidth());
        shader.setMatrix("uModel", model, matrixScratch);
        int color = entity.getRenderColor();
        shader.setUniform4f("uTint", ((color >> 16) & 255) / 255.0f, ((color >> 8) & 255) / 255.0f, (color & 255) / 255.0f, 1.0f);
        cube.render();
    }
}
