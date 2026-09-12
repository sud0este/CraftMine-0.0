package net.minecraft.util.math;

import net.minecraft.util.EnumFacing;

/** Compact value object for a block coordinate. */
public final class BlockPos {
    public final int x;
    public final int y;
    public final int z;

    public BlockPos(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public BlockPos add(int dx, int dy, int dz) {
        return new BlockPos(x + dx, y + dy, z + dz);
    }

    public BlockPos offset(EnumFacing facing) {
        return add(facing.getOffsetX(), facing.getOffsetY(), facing.getOffsetZ());
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof BlockPos)) return false;
        BlockPos other = (BlockPos) object;
        return x == other.x && y == other.y && z == other.z;
    }

    @Override
    public int hashCode() {
        int result = x * 73428767;
        result = 31 * result + y;
        return 31 * result + z;
    }

    @Override
    public String toString() {
        return "BlockPos{" + x + "," + y + "," + z + '}';
    }
}
