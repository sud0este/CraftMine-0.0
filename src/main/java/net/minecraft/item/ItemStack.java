package net.minecraft.item;

import net.minecraft.block.Block;

/** Quantity and damage pair; never share this object between inventory slots. */
public class ItemStack {
    private Item item;
    private int count;
    private int damage;

    public ItemStack(Item item, int count) { this(item, count, 0); }
    public ItemStack(Item item, int count, int damage) {
        this.item = item;
        this.count = Math.max(0, count);
        this.damage = Math.max(0, damage);
    }

    public static ItemStack empty() { return new ItemStack(null, 0); }
    public boolean isEmpty() { return item == null || count <= 0; }
    public Item getItem() { return item; }
    public int getCount() { return count; }
    public int getDamage() { return damage; }
    public void setCount(int count) { this.count = Math.max(0, count); if (this.count == 0) item = null; }
    public void grow(int amount) { setCount(count + amount); }
    public void shrink(int amount) { setCount(count - amount); }
    public void setDamage(int damage) { this.damage = Math.max(0, damage); }
    public int getMaxStackSize() { return item == null ? 0 : item.getMaxStackSize(); }
    public String getDisplayName() { return item == null ? "" : item.getName(); }
    public float getDestroySpeed(Block block) { return item == null ? 1.0f : item.getDestroySpeed(this, block); }
    public ItemStack copy() { return isEmpty() ? empty() : new ItemStack(item, count, damage); }

    public boolean isItemEqual(ItemStack other) {
        return !isEmpty() && other != null && !other.isEmpty() && item == other.item && damage == other.damage;
    }

    @Override public String toString() { return isEmpty() ? "ItemStack.EMPTY" : count + "x" + item.getName(); }
}
