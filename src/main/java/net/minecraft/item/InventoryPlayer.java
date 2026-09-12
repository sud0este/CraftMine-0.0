package net.minecraft.item;

import java.util.Arrays;

public final class InventoryPlayer {
    public static final int HOTBAR_SIZE = 9;
    public static final int MAIN_SIZE = 36;
    private final ItemStack[] mainInventory = new ItemStack[MAIN_SIZE];
    private final ItemStack[] craftingInventory = new ItemStack[4];
    private final ItemStack[] armorInventory = new ItemStack[4];
    private ItemStack cursorStack;
    private int currentItem;

    public InventoryPlayer() {
        Arrays.fill(mainInventory, null);
        Arrays.fill(craftingInventory, null);
        Arrays.fill(armorInventory, null);
    }

    public int getCurrentItem() { return currentItem; }
    public void setCurrentItem(int slot) { currentItem = Math.max(0, Math.min(HOTBAR_SIZE - 1, slot)); }
    public ItemStack getCurrentStack() { return mainInventory[currentItem]; }
    public ItemStack getStackInSlot(int slot) { return slot < 0 || slot >= MAIN_SIZE ? null : mainInventory[slot]; }
    public void setInventorySlotContents(int slot, ItemStack stack) { if (slot >= 0 && slot < MAIN_SIZE) mainInventory[slot] = clean(stack); }
    public ItemStack getCraftingStack(int slot) { return slot < 0 || slot >= 4 ? null : craftingInventory[slot]; }
    public void setCraftingStack(int slot, ItemStack stack) { if (slot >= 0 && slot < 4) craftingInventory[slot] = clean(stack); }
    public ItemStack getCursorStack() { return cursorStack; }
    public void setCursorStack(ItemStack stack) { cursorStack = clean(stack); }

    public boolean canAddItem(ItemStack incoming) {
        if (incoming == null || incoming.isEmpty()) return true;
        int remaining = incoming.getCount();
        for (ItemStack existing : mainInventory) {
            if (existing != null && existing.isItemEqual(incoming)) remaining -= Math.max(0, existing.getMaxStackSize() - existing.getCount());
        }
        for (ItemStack existing : mainInventory) if (existing == null || existing.isEmpty()) remaining -= incoming.getMaxStackSize();
        return remaining <= 0;
    }

    public boolean addItem(ItemStack incoming) {
        if (incoming == null || incoming.isEmpty()) return true;
        for (int i = 0; i < MAIN_SIZE && !incoming.isEmpty(); i++) {
            ItemStack existing = mainInventory[i];
            if (existing != null && existing.isItemEqual(incoming) && existing.getCount() < existing.getMaxStackSize()) {
                int moved = Math.min(incoming.getCount(), existing.getMaxStackSize() - existing.getCount());
                existing.grow(moved);
                incoming.shrink(moved);
            }
        }
        for (int i = 0; i < MAIN_SIZE && !incoming.isEmpty(); i++) {
            if (mainInventory[i] == null || mainInventory[i].isEmpty()) {
                int moved = Math.min(incoming.getCount(), incoming.getMaxStackSize());
                mainInventory[i] = new ItemStack(incoming.getItem(), moved, incoming.getDamage());
                incoming.shrink(moved);
            }
        }
        return incoming.isEmpty();
    }

    public void damageCurrentItem(int amount) {
        ItemStack stack = getCurrentStack();
        if (stack == null || stack.isEmpty() || stack.getItem().getMaxDamage() <= 0) return;
        stack.setDamage(stack.getDamage() + amount);
        if (stack.getDamage() >= stack.getItem().getMaxDamage()) stack.shrink(1);
    }

    public ItemStack[] getMainInventory() { return mainInventory; }
    public ItemStack[] getCraftingInventory() { return craftingInventory; }
    public ItemStack getArmorStack(int slot) { return slot < 0 || slot >= armorInventory.length ? null : armorInventory[slot]; }
    public void setArmorStack(int slot, ItemStack stack) { if (slot >= 0 && slot < armorInventory.length) armorInventory[slot] = clean(stack); }
    public int getArmorValue() {
        int value = 0;
        for (ItemStack stack : armorInventory) if (stack != null && !stack.isEmpty() && stack.getItem() instanceof ItemArmor) value += ((ItemArmor) stack.getItem()).getArmorPoints();
        return value;
    }

    private static ItemStack clean(ItemStack stack) { return stack == null || stack.isEmpty() ? null : stack; }
}
