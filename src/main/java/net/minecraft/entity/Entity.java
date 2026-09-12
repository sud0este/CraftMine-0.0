package net.minecraft.entity;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3;
import net.minecraft.world.World;

public class Entity {
    protected final World world;
    public double posX;
    public double posY;
    public double posZ;
    public double prevPosX;
    public double prevPosY;
    public double prevPosZ;
    public double motionX;
    public double motionY;
    public double motionZ;
    public float rotationYaw;
    public float rotationPitch;
    protected float width = 0.6f;
    protected float height = 1.8f;
    protected float yOffset;
    protected AxisAlignedBB boundingBox;
    protected boolean onGround;
    protected boolean collidedHorizontally;
    protected boolean noClip;
    protected boolean dead;
    protected float fallDistance;

    public Entity(World world) {
        this.world = world;
        setSize(width, height);
    }

    public Entity(World world, double x, double y, double z) {
        this(world);
        setPosition(x, y, z);
    }

    public World getWorld() { return world; }
    public AxisAlignedBB getBoundingBox() { return boundingBox; }
    public boolean isOnGround() { return onGround; }
    public boolean isDead() { return dead; }
    public void setDead() { dead = true; }
    public float getWidth() { return width; }
    public float getHeight() { return height; }

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
        this.yOffset = height * 0.85f;
        if (boundingBox != null) setPosition(posX, posY, posZ);
    }

    public void setPosition(double x, double y, double z) {
        posX = x; posY = y; posZ = z;
        float halfWidth = width / 2.0f;
        boundingBox = new AxisAlignedBB(x - halfWidth, y, z - halfWidth, x + halfWidth, y + height, z + halfWidth);
    }

    public Vec3 getPositionEyes(float partialTicks) {
        return new Vec3(posX, posY + getEyeHeight(), posZ);
    }

    public float getEyeHeight() { return height * 0.85f; }

    public Vec3 getLookVec() {
        float yaw = rotationYaw * (float) Math.PI / 180.0f;
        float pitch = rotationPitch * (float) Math.PI / 180.0f;
        double cosPitch = Math.cos(pitch);
        return new Vec3(-Math.sin(yaw) * cosPitch, -Math.sin(pitch), Math.cos(yaw) * cosPitch).normalize();
    }

    public double getDistanceSq(double x, double y, double z) {
        double dx = posX - x, dy = posY - y, dz = posZ - z;
        return dx * dx + dy * dy + dz * dz;
    }

    public double getDistanceSq(Entity entity) { return getDistanceSq(entity.posX, entity.posY, entity.posZ); }

    public void onUpdate() {
        prevPosX = posX; prevPosY = posY; prevPosZ = posZ;
    }

    public void move(double x, double y, double z) {
        if (noClip) { setPosition(posX + x, posY + y, posZ + z); return; }
        AxisAlignedBB original = boundingBox;
        AxisAlignedBB swept = boundingBox.addCoord(x, y, z).expand(0.001, 0.001, 0.001);
        java.util.List<AxisAlignedBB> collisions = world.getCollisionBoxes(this, swept);
        double adjustedY = y;
        for (AxisAlignedBB collision : collisions) adjustedY = collision.calculateYOffset(boundingBox, adjustedY);
        boundingBox = boundingBox.offset(0, adjustedY, 0);
        double adjustedX = x;
        for (AxisAlignedBB collision : collisions) adjustedX = collision.calculateXOffset(boundingBox, adjustedX);
        boundingBox = boundingBox.offset(adjustedX, 0, 0);
        double adjustedZ = z;
        for (AxisAlignedBB collision : collisions) adjustedZ = collision.calculateZOffset(boundingBox, adjustedZ);
        boundingBox = boundingBox.offset(0, 0, adjustedZ);
        onGround = y < 0 && adjustedY != y;
        collidedHorizontally = adjustedX != x || adjustedZ != z;
        if (y != adjustedY && y < 0) fallDistance = 0;
        posX = (boundingBox.minX + boundingBox.maxX) / 2.0;
        posY = boundingBox.minY;
        posZ = (boundingBox.minZ + boundingBox.maxZ) / 2.0;
        if (original == null) setPosition(posX, posY, posZ);
    }

    public int getRenderColor() { return 0xFFFFFF; }
}
