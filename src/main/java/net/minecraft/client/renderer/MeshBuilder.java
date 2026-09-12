package net.minecraft.client.renderer;

import java.util.Arrays;

import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

/** CPU side visible-face mesher. Each vertex is position, atlas UV, tint RGB and light. */
public final class MeshBuilder {
    private float[] data = new float[4096];
    private int size;
    private final TextureAtlas atlas;

    public MeshBuilder(TextureAtlas atlas) { this.atlas = atlas; }
    public float[] getData() { return Arrays.copyOf(data, size); }
    public int getFloatCount() { return size; }
    public int getVertexCount() { return size / 9; }

    public void build(World world, Chunk chunk) {
        size = 0;
        int originX = chunk.getChunkX() * 16, originZ = chunk.getChunkZ() * 16;
        for (int y = 0; y < Chunk.HEIGHT; y++) for (int z = 0; z < 16; z++) for (int x = 0; x < 16; x++) {
            Block block = chunk.getBlock(x, y, z);
            if (block.isAir()) continue;
            for (EnumFacing face : EnumFacing.values()) {
                int nx = originX + x + face.getOffsetX(), ny = y + face.getOffsetY(), nz = originZ + z + face.getOffsetZ();
                Block neighbor = world.getBlockState(nx, ny, nz).getBlock();
                if (!block.shouldSideBeRendered(neighbor, face)) continue;
                addFace(block, face, originX + x, y, originZ + z, world.getLightBrightness(nx, ny, nz));
            }
        }
    }

    private void addFace(Block block, EnumFacing face, int x, int y, int z, float light) {
        float[] vertices;
        float normalLight;
        switch (face) {
            case DOWN:
                vertices = new float[] {x,y,z+1, x+1,y,z+1, x+1,y,z, x,y,z, x,y,z+1, x+1,y,z}; normalLight = 0.55f; break;
            case UP:
                vertices = new float[] {x,y+1,z, x+1,y+1,z, x+1,y+1,z+1, x,y+1,z+1, x+1,y+1,z+1, x,y+1,z}; normalLight = 1.0f; break;
            case NORTH:
                vertices = new float[] {x+1,y,z, x,y,z, x,y+1,z, x+1,y,z, x,y+1,z, x+1,y+1,z}; normalLight = 0.72f; break;
            case SOUTH:
                vertices = new float[] {x,y,z+1, x+1,y+1,z+1, x,y+1,z+1, x,y,z+1, x+1,y,z+1, x+1,y+1,z+1}; normalLight = 0.86f; break;
            case WEST:
                vertices = new float[] {x,y,z, x,y,z+1, x,y+1,z+1, x,y,z, x,y+1,z+1, x,y+1,z}; normalLight = 0.65f; break;
            default:
                vertices = new float[] {x+1,y,z+1, x+1,y+1,z+1, x+1,y,z, x+1,y,z, x+1,y+1,z+1, x+1,y+1,z}; normalLight = 0.80f; break;
        }
        int tile = block.getTexture(face);
        float u0 = atlas.u0(tile), u1 = atlas.u1(tile), v0 = atlas.v0(tile), v1 = atlas.v1(tile);
        float[] uvs = {u0,v1, u1,v1, u1,v0, u0,v0, u1,v0, u0,v1};
        int color = block.getRenderColor(0);
        float r = ((color >> 16) & 255) / 255.0f, g = ((color >> 8) & 255) / 255.0f, b = (color & 255) / 255.0f;
        for (int vertex = 0; vertex < 6; vertex++) {
            ensure(9);
            int vi = vertex * 3;
            put(vertices[vi]); put(vertices[vi + 1]); put(vertices[vi + 2]);
            put(uvs[vertex * 2]); put(uvs[vertex * 2 + 1]);
            put(r); put(g); put(b); put(Math.max(0.1f, Math.min(1.0f, light * normalLight)));
        }
    }

    private void put(float value) { data[size++] = value; }
    private void ensure(int count) { if (size + count > data.length) data = Arrays.copyOf(data, Math.max(data.length * 2, size + count)); }
}
