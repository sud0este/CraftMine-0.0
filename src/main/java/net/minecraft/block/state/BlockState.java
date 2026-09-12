package net.minecraft.block.state;

import net.minecraft.block.Block;

/** The compact id/meta pair used by a chunk. */
public final class BlockState {
    private final Block block;
    private final int metadata;

    public BlockState(Block block, int metadata) {
        this.block = block;
        this.metadata = metadata & 15;
    }

    public Block getBlock() { return block; }
    public int getMetadata() { return metadata; }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof BlockState)) return false;
        BlockState other = (BlockState) object;
        return block == other.block && metadata == other.metadata;
    }

    @Override
    public int hashCode() { return block.getId() * 31 + metadata; }
}
