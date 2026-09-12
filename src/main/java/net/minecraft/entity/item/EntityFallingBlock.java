package net.minecraft.entity.item;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public final class EntityFallingBlock extends Entity {
    private final Block block;
    private int age;
    public EntityFallingBlock(World world, double x, double y, double z, Block block) {
        super(world, x, y, z); this.block = block; setSize(0.98f, 0.98f);
    }
    @Override public void onUpdate() {
        super.onUpdate();
        motionY -= 0.04; move(0, motionY, 0);
        if (onGround || ++age > 200) {
            int x = (int) Math.floor(posX), y = (int) Math.floor(posY), z = (int) Math.floor(posZ);
            if (world.getBlockState(x, y, z).getBlock().isAir()) world.setBlockState(x, y, z, block.getDefaultState());
            setDead();
        }
    }
    @Override public int getRenderColor() { return block.getRenderColor(0); }
}
