package net.minecraft.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3;

public final class RayTraceResult {
    public enum Type { MISS, BLOCK, ENTITY }

    public final Type type;
    public final Vec3 hitVec;
    public final BlockPos blockPos;
    public final EnumFacing sideHit;
    public final Entity entityHit;

    private RayTraceResult(Type type, Vec3 hitVec, BlockPos blockPos, EnumFacing sideHit, Entity entityHit) {
        this.type = type;
        this.hitVec = hitVec;
        this.blockPos = blockPos;
        this.sideHit = sideHit;
        this.entityHit = entityHit;
    }

    public static RayTraceResult miss(Vec3 point) {
        return new RayTraceResult(Type.MISS, point, null, null, null);
    }

    public static RayTraceResult block(Vec3 point, EnumFacing side, BlockPos pos) {
        return new RayTraceResult(Type.BLOCK, point, pos, side, null);
    }

    public static RayTraceResult entity(Vec3 point, Entity entity) {
        return new RayTraceResult(Type.ENTITY, point, null, null, entity);
    }
}
