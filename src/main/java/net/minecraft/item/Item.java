package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/** Base item. Items are immutable registry entries; mutable quantities live in ItemStack. */
public class Item {
    private final int id;
    private final String name;
    private final int maxStackSize;
    private final int maxDamage;

    public Item(int id, String name) { this(id, name, 64, 0); }
    public Item(int id, String name, int maxStackSize, int maxDamage) {
        this.id = id;
        this.name = name;
        this.maxStackSize = maxStackSize;
        this.maxDamage = maxDamage;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getMaxStackSize() { return maxStackSize; }
    public int getMaxDamage() { return maxDamage; }
    public float getDestroySpeed(ItemStack stack, Block block) { return 1.0f; }
    public boolean canHarvestBlock(ItemStack stack, Block block) { return block.getHardness() >= 0.0f && block.getHardness() <= 0.6f; }
    public int getColor() { return 0xFFFFFF; }

    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world,
                             BlockPos pos, EnumFacing side) { return false; }

    public ItemStack onItemRightClick(ItemStack stack, EntityPlayer player, World world) { return stack; }

    @Override public String toString() { return "Item{" + name + "#" + id + '}'; }
}
