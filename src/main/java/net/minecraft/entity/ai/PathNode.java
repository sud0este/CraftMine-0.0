package net.minecraft.entity.ai;

public final class PathNode {
    public final int x, y, z;
    public PathNode parent;
    public float cost;
    public PathNode(int x, int y, int z) { this.x = x; this.y = y; this.z = z; }
    @Override public boolean equals(Object object) { return object instanceof PathNode && ((PathNode) object).x == x && ((PathNode) object).y == y && ((PathNode) object).z == z; }
    @Override public int hashCode() { return (x * 73428767) ^ (y * 912931) ^ z; }
}
