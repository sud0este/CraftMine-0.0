package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Common block definition. A block is immutable and registered once; metadata is
 * kept in Chunk, just as in the compact 1.8 era storage format.
 */
public class Block {
    private final int id;
    private final String name;
    private final Material material;
    private final int texture;
    private final boolean opaqueCube;
    private final boolean fullCube;
    private final boolean translucent;
    private final float hardness;
    private final int lightValue;

    public Block(int id, String name, Material material, int texture, float hardness) {
        this(id, name, material, texture, material.isOpaque(), material.blocksMovement(), false, hardness, 0);
    }

    public Block(int id, String name, Material material, int texture, boolean opaqueCube,
                 boolean fullCube, boolean translucent, float hardness, int lightValue) {
        this.id = id;
        this.name = name;
        this.material = material;
        this.texture = texture;
        this.opaqueCube = opaqueCube;
        this.fullCube = fullCube;
        this.translucent = translucent;
        this.hardness = hardness;
        this.lightValue = lightValue;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public Material getMaterial() { return material; }
    public int getTexture(EnumFacing face) { return texture; }
    public boolean isOpaqueCube() { return opaqueCube; }
    public boolean isFullCube() { return fullCube; }
    public boolean isTranslucent() { return translucent; }
    public boolean isAir() { return false; }
    public boolean isSolid() { return material.blocksMovement(); }
    public float getHardness() { return hardness; }
    public int getLightValue() { return lightValue; }

    public AxisAlignedBB getCollisionBoundingBox(World world, int x, int y, int z) {
        return fullCube ? AxisAlignedBB.fromBlock(x, y, z) : null;
    }

    public boolean shouldSideBeRendered(Block neighbor, EnumFacing face) {
        return !neighbor.isOpaqueCube() || neighbor != this;
    }

    public int getRenderColor(int metadata) { return 0xFFFFFF; }

    public ItemStack[] getDrops(World world, BlockPos pos, int metadata, ItemStack tool) {
        net.minecraft.item.Item item = ItemRegistry.getBlockItem(this);
        return item == null ? new ItemStack[0] : new ItemStack[] { new ItemStack(item, 1) };
    }

    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        return world.getBlockState(pos).getBlock().isReplaceable();
    }

    public boolean isReplaceable() { return isAir(); }

    public void onBlockAdded(World world, BlockPos pos) { }
    public void onBlockRemoved(World world, BlockPos pos) { }
    public boolean onBlockActivated(World world, BlockPos pos, EntityPlayer player) { return false; }

    public BlockState getDefaultState() { return new BlockState(this, 0); }

    @Override
    public String toString() { return "Block{" + name + "#" + id + '}'; }
}
