package net.minecraft.util.math;

/** Mutable-free axis aligned box, matching the small collision primitive used by the legacy client. */
public final class AxisAlignedBB {
    public final double minX;
    public final double minY;
    public final double minZ;
    public final double maxX;
    public final double maxY;
    public final double maxZ;

    public AxisAlignedBB(double minX, double minY, double minZ,
                         double maxX, double maxY, double maxZ) {
        this.minX = Math.min(minX, maxX);
        this.minY = Math.min(minY, maxY);
        this.minZ = Math.min(minZ, maxZ);
        this.maxX = Math.max(minX, maxX);
        this.maxY = Math.max(minY, maxY);
        this.maxZ = Math.max(minZ, maxZ);
    }

    public AxisAlignedBB addCoord(double x, double y, double z) {
        double nMinX = x < 0 ? minX + x : minX;
        double nMaxX = x > 0 ? maxX + x : maxX;
        double nMinY = y < 0 ? minY + y : minY;
        double nMaxY = y > 0 ? maxY + y : maxY;
        double nMinZ = z < 0 ? minZ + z : minZ;
        double nMaxZ = z > 0 ? maxZ + z : maxZ;
        return new AxisAlignedBB(nMinX, nMinY, nMinZ, nMaxX, nMaxY, nMaxZ);
    }

    public AxisAlignedBB expand(double x, double y, double z) {
        return new AxisAlignedBB(minX - x, minY - y, minZ - z,
                maxX + x, maxY + y, maxZ + z);
    }

    public AxisAlignedBB offset(double x, double y, double z) {
        return new AxisAlignedBB(minX + x, minY + y, minZ + z,
                maxX + x, maxY + y, maxZ + z);
    }

    public boolean intersects(AxisAlignedBB other) {
        return other.maxX > minX && other.minX < maxX
                && other.maxY > minY && other.minY < maxY
                && other.maxZ > minZ && other.minZ < maxZ;
    }

    public double calculateXOffset(AxisAlignedBB other, double offset) {
        if (other.maxY <= minY || other.minY >= maxY || other.maxZ <= minZ || other.minZ >= maxZ) return offset;
        if (offset > 0.0 && other.maxX <= minX) {
            double distance = minX - other.maxX;
            if (distance < offset) offset = distance;
        } else if (offset < 0.0 && other.minX >= maxX) {
            double distance = maxX - other.minX;
            if (distance > offset) offset = distance;
        }
        return offset;
    }

    public double calculateYOffset(AxisAlignedBB other, double offset) {
        if (other.maxX <= minX || other.minX >= maxX || other.maxZ <= minZ || other.minZ >= maxZ) return offset;
        if (offset > 0.0 && other.maxY <= minY) {
            double distance = minY - other.maxY;
            if (distance < offset) offset = distance;
        } else if (offset < 0.0 && other.minY >= maxY) {
            double distance = maxY - other.minY;
            if (distance > offset) offset = distance;
        }
        return offset;
    }

    public double calculateZOffset(AxisAlignedBB other, double offset) {
        if (other.maxX <= minX || other.minX >= maxX || other.maxY <= minY || other.minY >= maxY) return offset;
        if (offset > 0.0 && other.maxZ <= minZ) {
            double distance = minZ - other.maxZ;
            if (distance < offset) offset = distance;
        } else if (offset < 0.0 && other.minZ >= maxZ) {
            double distance = maxZ - other.minZ;
            if (distance > offset) offset = distance;
        }
        return offset;
    }

    public static AxisAlignedBB fromBlock(int x, int y, int z) {
        return new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);
    }
}
