package net.minecraft.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRegistry;
import net.minecraft.block.state.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.RayTraceResult;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkProviderServer;
import net.minecraft.world.lighting.LightingEngine;

/** Singleplayer server world: owns chunks, entity ticks, time and collision queries. */
public class World {
    public static final int MIN_HEIGHT = 0;
    public static final int MAX_HEIGHT = 256;
    private final long seed;
    private final Random random;
    private final ChunkProviderServer chunkProvider;
    private final LightingEngine lightingEngine;
    private final List<Entity> loadedEntityList = new ArrayList<Entity>();
    private EntityPlayer player;
    private long worldTime;
    private int mobSpawnCooldown;

    public World() { this(System.currentTimeMillis()); }

    public World(long seed) {
        this.seed = seed;
        this.random = new Random(seed);
        this.lightingEngine = new LightingEngine(this);
        this.chunkProvider = new ChunkProviderServer(this, seed);
    }

    public long getSeed() { return seed; }
    public long getWorldTime() { return worldTime; }
    public Random getRandom() { return random; }
    public LightingEngine getLightingEngine() { return lightingEngine; }
    public ChunkProviderServer getChunkProvider() { return chunkProvider; }
    public EntityPlayer getPlayer() { return player; }
    public void setPlayer(EntityPlayer player) { this.player = player; addEntity(player); }
    public List<Entity> getLoadedEntityList() { return Collections.unmodifiableList(loadedEntityList); }

    public Chunk getChunkFromChunkCoords(int chunkX, int chunkZ) { return chunkProvider.provideChunk(chunkX, chunkZ); }
    public Chunk getChunkFromBlockCoords(int x, int z) { return getChunkFromChunkCoords(Math.floorDiv(x, 16), Math.floorDiv(z, 16)); }

    public boolean isValidBuildHeight(int y) { return y >= MIN_HEIGHT && y < MAX_HEIGHT; }

    public BlockState getBlockState(BlockPos pos) { return getBlockState(pos.x, pos.y, pos.z); }
    public BlockState getBlockState(int x, int y, int z) {
        if (!isValidBuildHeight(y)) return BlockRegistry.AIR.getDefaultState();
        Chunk chunk = getChunkFromBlockCoords(x, z);
        return chunk.getBlockState(Math.floorMod(x, 16), y, Math.floorMod(z, 16));
    }

    public boolean setBlockState(BlockPos pos, BlockState state) { return setBlockState(pos.x, pos.y, pos.z, state); }
    public boolean setBlockState(int x, int y, int z, BlockState state) {
        if (!isValidBuildHeight(y)) return false;
        Chunk chunk = getChunkFromBlockCoords(x, z);
        int localX = Math.floorMod(x, 16), localZ = Math.floorMod(z, 16);
        Block old = chunk.getBlock(localX, y, localZ);
        if (old == state.getBlock()) return false;
        chunk.setBlockState(localX, y, localZ, state);
        if (localX == 0) getChunkFromChunkCoords(chunk.getChunkX() - 1, chunk.getChunkZ()).markDirty();
        if (localX == 15) getChunkFromChunkCoords(chunk.getChunkX() + 1, chunk.getChunkZ()).markDirty();
        if (localZ == 0) getChunkFromChunkCoords(chunk.getChunkX(), chunk.getChunkZ() - 1).markDirty();
        if (localZ == 15) getChunkFromChunkCoords(chunk.getChunkX(), chunk.getChunkZ() + 1).markDirty();
        old.onBlockRemoved(this, new BlockPos(x, y, z));
        state.getBlock().onBlockAdded(this, new BlockPos(x, y, z));
        lightingEngine.updateAt(x, y, z);
        return true;
    }

    public int getHeight(int x, int z) {
        Chunk chunk = getChunkFromBlockCoords(x, z);
        int localX = Math.floorMod(x, 16), localZ = Math.floorMod(z, 16);
        for (int y = MAX_HEIGHT - 1; y >= 0; y--) if (chunk.getBlock(localX, y, localZ).isSolid()) return y + 1;
        return 1;
    }

    public int getTopSolidOrLiquidBlock(int x, int z) { return getHeight(x, z); }
    public Biome getBiome(int x, int z) { return chunkProvider.getGenerator().getBiome(x, z); }

    public List<AxisAlignedBB> getCollisionBoxes(Entity entity, AxisAlignedBB box) {
        List<AxisAlignedBB> boxes = new ArrayList<AxisAlignedBB>();
        int minX = MathHelper.floor(box.minX), maxX = MathHelper.floor(box.maxX + 1.0);
        int minY = Math.max(0, MathHelper.floor(box.minY)), maxY = Math.min(MAX_HEIGHT - 1, MathHelper.floor(box.maxY + 1.0));
        int minZ = MathHelper.floor(box.minZ), maxZ = MathHelper.floor(box.maxZ + 1.0);
        for (int x = minX; x <= maxX; x++) for (int y = minY; y <= maxY; y++) for (int z = minZ; z <= maxZ; z++) {
            Block block = getBlockState(x, y, z).getBlock();
            AxisAlignedBB collision = block.getCollisionBoundingBox(this, x, y, z);
            if (collision != null && collision.intersects(box)) boxes.add(collision);
        }
        return boxes;
    }

    public boolean isBlockLoaded(int x, int z) { return true; }
    public boolean isFaceOpaque(int x, int y, int z) { return getBlockState(x, y, z).getBlock().isOpaqueCube(); }

    public int getSkyLight(int x, int y, int z) {
        if (!isValidBuildHeight(y)) return y >= MAX_HEIGHT ? 15 : 0;
        Chunk chunk = getChunkFromBlockCoords(x, z);
        return Math.max(0, chunk.getSkyLight(Math.floorMod(x, 16), y, Math.floorMod(z, 16)) - getSkylightSubtracted());
    }

    public int getBlockLight(int x, int y, int z) {
        if (!isValidBuildHeight(y)) return 0;
        Chunk chunk = getChunkFromBlockCoords(x, z);
        return chunk.getBlockLight(Math.floorMod(x, 16), y, Math.floorMod(z, 16));
    }

    public float getLightBrightness(int x, int y, int z) {
        return Math.max(getSkyLight(x, y, z), getBlockLight(x, y, z)) / 15.0f;
    }

    public int getSkylightSubtracted() {
        float angle = getCelestialAngle(1.0f);
        float brightness = 1.0f - (float) (Math.cos(angle * Math.PI * 2.0) * 2.0 + 0.5);
        brightness = Math.max(0.0f, Math.min(1.0f, brightness));
        return (int) (brightness * 11.0f);
    }

    public float getCelestialAngle(float partialTicks) { return ((worldTime % 24000L) + partialTicks) / 24000.0f - 0.25f; }
    public boolean isDaytime() { long time = worldTime % 24000L; return time < 12000L; }

    public RayTraceResult rayTraceBlocks(Vec3 start, Vec3 end, boolean stopOnLiquid) {
        Vec3 delta = end.subtract(start);
        double distance = delta.length();
        int steps = Math.max(1, (int) (distance * 12.0));
        Vec3 previous = start;
        int previousX = MathHelper.floor(previous.x), previousY = MathHelper.floor(previous.y), previousZ = MathHelper.floor(previous.z);
        for (int i = 1; i <= steps; i++) {
            double t = i / (double) steps;
            Vec3 point = new Vec3(start.x + delta.x * t, start.y + delta.y * t, start.z + delta.z * t);
            int x = MathHelper.floor(point.x), y = MathHelper.floor(point.y), z = MathHelper.floor(point.z);
            Block block = getBlockState(x, y, z).getBlock();
            if (!block.isAir() && (stopOnLiquid || block != BlockRegistry.WATER)) {
                EnumFacing side;
                if (x != previousX) side = x > previousX ? EnumFacing.WEST : EnumFacing.EAST;
                else if (y != previousY) side = y > previousY ? EnumFacing.DOWN : EnumFacing.UP;
                else side = z > previousZ ? EnumFacing.NORTH : EnumFacing.SOUTH;
                return RayTraceResult.block(point, side, new BlockPos(x, y, z));
            }
            previous = point; previousX = x; previousY = y; previousZ = z;
        }
        return RayTraceResult.miss(end);
    }

    public void addEntity(Entity entity) { if (!loadedEntityList.contains(entity)) loadedEntityList.add(entity); }
    public void removeEntity(Entity entity) { entity.setDead(); }

    public <T extends Entity> List<T> getEntitiesWithinAABB(Class<T> type, AxisAlignedBB box, Entity excluded) {
        List<T> result = new ArrayList<T>();
        for (Entity entity : loadedEntityList) {
            if (entity == excluded || entity.isDead() || !type.isInstance(entity)) continue;
            if (entity.getBoundingBox().intersects(box)) result.add(type.cast(entity));
        }
        return result;
    }

    public void spawnEntity(Entity entity) { addEntity(entity); }

    public void tick() {
        worldTime++;
        for (int i = 0; i < loadedEntityList.size(); i++) {
            Entity entity = loadedEntityList.get(i);
            if (entity.isDead()) { loadedEntityList.remove(i--); continue; }
            entity.onUpdate();
        }
        if (mobSpawnCooldown-- <= 0) { mobSpawnCooldown = 20; spawnMobs(); }
    }

    private void spawnMobs() {
        if (player == null || loadedEntityList.size() > 40) return;
        for (int attempt = 0; attempt < 2; attempt++) {
            int x = (int) player.posX - 20 + random.nextInt(41);
            int z = (int) player.posZ - 20 + random.nextInt(41);
            int y = getTopSolidOrLiquidBlock(x, z);
            if (y <= 1 || Math.abs(x - player.posX) < 8 && Math.abs(z - player.posZ) < 8) continue;
            boolean inWater = getBlockState(x, y, z).getBlock() == BlockRegistry.WATER;
            Entity entity;
            if (inWater) {
                entity = new net.minecraft.entity.passive.EntitySquid(this, x + 0.5, y, z + 0.5);
            } else {
                boolean hostile = !isDaytime() && getSkyLight(x, y + 1, z) < 8;
                if (hostile) {
                    switch (random.nextInt(5)) {
                        case 0: entity = new EntityZombie(this, x + 0.5, y, z + 0.5); break;
                        case 1: entity = new EntitySkeleton(this, x + 0.5, y, z + 0.5); break;
                        case 2: entity = new EntityCreeper(this, x + 0.5, y, z + 0.5); break;
                        case 3: entity = new EntitySpider(this, x + 0.5, y, z + 0.5); break;
                        default: entity = new EntityEnderman(this, x + 0.5, y, z + 0.5); break;
                    }
                } else {
                    switch (random.nextInt(4)) {
                        case 0: entity = new EntityCow(this, x + 0.5, y, z + 0.5); break;
                        case 1: entity = new EntityPig(this, x + 0.5, y, z + 0.5); break;
                        case 2: entity = new EntitySheep(this, x + 0.5, y, z + 0.5); break;
                        default: entity = new EntityChicken(this, x + 0.5, y, z + 0.5); break;
                    }
                }
            }
            addEntity(entity);
        }
    }

    public void createExplosion(Entity source, double x, double y, double z, float power) {
        int radius = (int) power;
        for (int dx = -radius; dx <= radius; dx++) for (int dy = -radius; dy <= radius; dy++) for (int dz = -radius; dz <= radius; dz++) {
            if (dx * dx + dy * dy + dz * dz <= radius * radius && random.nextFloat() < 0.72f)
                setBlockState((int) x + dx, (int) y + dy, (int) z + dz, BlockRegistry.AIR.getDefaultState());
        }
        AxisAlignedBB damageBox = new AxisAlignedBB(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius);
        for (EntityLivingBase living : getEntitiesWithinAABB(EntityLivingBase.class, damageBox, source)) {
            double distance = Math.sqrt(living.getDistanceSq(x, y, z));
            living.attackEntityFrom(DamageSource.GENERIC, Math.max(1.0f, power * (1.0f - distance / (radius + 0.01))));
        }
    }
}
