package net.minecraft.client.renderer;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.system.MemoryUtil;

import net.minecraft.client.Window;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

/** Fixed-function responsibilities from the old client expressed with small GL3 shaders. */
public final class WorldRenderer {
    private final Window window;
    private final GameSettings settings;
    private final ShaderProgram worldShader;
    private final TextureAtlas atlas;
    private final CubeModel entityCube;
    private final EntityRenderer entityRenderer;
    private final Map<Long, ChunkMesh> meshes = new HashMap<Long, ChunkMesh>();
    private final FloatBuffer matrixScratch = MemoryUtil.memAllocFloat(16);

    public WorldRenderer(Window window, GameSettings settings) {
        this.window = window;
        this.settings = settings;
        worldShader = new ShaderProgram(ShaderProgram.load("/assets/craftmine/shaders/world.vert"), ShaderProgram.load("/assets/craftmine/shaders/world.frag"));
        atlas = new TextureAtlas();
        entityCube = new CubeModel();
        entityRenderer = new EntityRenderer(entityCube, worldShader, matrixScratch);
    }

    public void render(World world, EntityPlayer player, float partialTicks) {
        int width = window.getWidth(), height = Math.max(1, window.getHeight());
        GL11.glViewport(0, 0, width, height);
        float day = (float) Math.max(0.0, Math.cos(world.getCelestialAngle(partialTicks) * Math.PI * 2.0));
        GL11.glClearColor(0.38f * (0.45f + day * 0.55f), 0.62f * (0.45f + day * 0.55f), 0.92f * (0.45f + day * 0.55f), 1.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        // Visibility culling is done by MeshBuilder; keeping GL culling off makes every face winding portable.
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        Vec3 eye = new Vec3(player.prevPosX + (player.posX - player.prevPosX) * partialTicks,
                player.prevPosY + (player.posY - player.prevPosY) * partialTicks + player.getEyeHeight(),
                player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks);
        Vec3 look = player.getLookVec();
        Matrix4f projection = Matrix4f.perspective((float) Math.toRadians(70.0), width / (float) height, 0.05f, settings.renderDistance * 16.0f + 32.0f);
        Matrix4f viewProjection = projection.multiply(Matrix4f.lookAt(eye, eye.add(look), new Vec3(0, 1, 0)));

        worldShader.use();
        worldShader.setMatrix("uViewProjection", viewProjection, matrixScratch);
        worldShader.setMatrix("uModel", new Matrix4f(), matrixScratch);
        worldShader.setUniform4f("uTint", 1, 1, 1, 1);
        worldShader.setUniform1i("uAtlas", 0);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        atlas.bind();

        int centerX = (int) Math.floor(player.posX) >> 4;
        int centerZ = (int) Math.floor(player.posZ) >> 4;
        int distance = settings.renderDistance;
        for (int dz = -distance; dz <= distance; dz++) for (int dx = -distance; dx <= distance; dx++) {
            if (dx * dx + dz * dz > distance * distance + distance) continue;
            Chunk chunk = world.getChunkFromChunkCoords(centerX + dx, centerZ + dz);
            long key = (((long) chunk.getChunkX()) << 32) ^ (chunk.getChunkZ() & 0xFFFFFFFFL);
            ChunkMesh mesh = meshes.get(key);
            if (mesh == null) { mesh = new ChunkMesh(atlas); meshes.put(key, mesh); }
            mesh.rebuild(world, chunk);
            mesh.render();
        }

        worldShader.setUniform4f("uTint", 1, 1, 1, 1);
        for (Entity entity : world.getLoadedEntityList()) {
            if (entity == player || entity.isDead()) continue;
            if (entity.getDistanceSq(player) <= (distance * 16 + 16) * (distance * 16 + 16)) entityRenderer.render(entity, viewProjection, eye, partialTicks);
        }
        GL11.glDisable(GL11.GL_CULL_FACE);
    }

    public void close() {
        for (ChunkMesh mesh : meshes.values()) mesh.delete();
        entityCube.delete(); atlas.delete(); worldShader.delete(); MemoryUtil.memFree(matrixScratch);
    }
}
