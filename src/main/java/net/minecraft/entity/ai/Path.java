package net.minecraft.entity.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Path {
    private final List<PathNode> nodes;
    private int index;
    public Path(List<PathNode> nodes) { this.nodes = new ArrayList<PathNode>(nodes); }
    public boolean isFinished() { return index >= nodes.size(); }
    public PathNode getCurrentNode() { return isFinished() ? null : nodes.get(index); }
    public void advance() { if (!isFinished()) index++; }
    public List<PathNode> getNodes() { return Collections.unmodifiableList(nodes); }
}
