package net.minecraft.entity.ai;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

/** Small bounded breadth-first navigator for mobs; direct steering remains the cheap common case. */
public final class PathNavigate {
    private final EntityLivingBase entity;
    private final World world;
    private Path path;
    public PathNavigate(EntityLivingBase entity) { this.entity = entity; this.world = entity.getWorld(); }
    public boolean tryMoveTo(double x, double y, double z, double speed) {
        path = findPath(MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z), 12);
        if (path == null) return false;
        tick(speed); return true;
    }
    public void tick(double speed) {
        if (path == null || path.isFinished()) return;
        PathNode node = path.getCurrentNode();
        double dx = node.x + 0.5 - entity.posX, dz = node.z + 0.5 - entity.posZ;
        if (dx * dx + dz * dz < 0.4) { path.advance(); return; }
        double length = Math.sqrt(dx * dx + dz * dz);
        entity.motionX += dx / length * speed;
        entity.motionZ += dz / length * speed;
    }
    public Path getPath() { return path; }

    private Path findPath(int targetX, int targetY, int targetZ, int radius) {
        PathNode start = new PathNode(MathHelper.floor(entity.posX), MathHelper.floor(entity.posY), MathHelper.floor(entity.posZ));
        Queue<PathNode> queue = new ArrayDeque<PathNode>(); Map<PathNode, Integer> depth = new HashMap<PathNode, Integer>(); Set<PathNode> seen = new HashSet<PathNode>();
        queue.add(start); depth.put(start, 0); seen.add(start);
        int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
        while (!queue.isEmpty()) {
            PathNode current = queue.remove();
            if (current.x == targetX && current.z == targetZ) return build(current);
            if (depth.get(current) >= radius) continue;
            for (int[] dir : dirs) {
                int nx = current.x + dir[0], nz = current.z + dir[1];
                PathNode next = new PathNode(nx, current.y, nz);
                if (seen.contains(next) || !passable(nx, current.y, nz)) continue;
                next.parent = current; seen.add(next); depth.put(next, depth.get(current) + 1); queue.add(next);
            }
        }
        return null;
    }
    private boolean passable(int x, int y, int z) { return world.getBlockState(x, y, z).getBlock().isAir() && world.getBlockState(x, y - 1, z).getBlock().isSolid(); }
    private static Path build(PathNode node) { java.util.LinkedList<PathNode> nodes = new java.util.LinkedList<PathNode>(); while (node != null) { nodes.addFirst(node); node = node.parent; } return new Path(nodes); }
}
