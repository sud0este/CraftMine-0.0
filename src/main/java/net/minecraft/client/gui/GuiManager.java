package net.minecraft.client.gui;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.Window;
import net.minecraft.client.input.Input;
import net.minecraft.crafting.CraftingManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/** Inventory/hotbar UI and the 2x2/3x3 crafting interaction. */
public final class GuiManager {
    private final Window window;
    private final Input input;
    private final UiRenderer ui;
    private boolean open;
    private boolean craftingTable;

    public GuiManager(Window window, Input input) { this.window = window; this.input = input; ui = new UiRenderer(window); }
    public boolean isOpen() { return open; }
    public boolean isCraftingTable() { return craftingTable; }
    public void toggleInventory(EntityPlayer player) { if (open) close(player); else openInventory(player, false); }
    public void openInventory(EntityPlayer player, boolean table) { open = true; craftingTable = table; player.setGuiOpen(true); input.setCursorCaptured(false); }
    public void openCraftingTable(EntityPlayer player) { openInventory(player, true); }
    public void close(EntityPlayer player) { open = false; craftingTable = false; player.setGuiOpen(false); input.setCursorCaptured(true); }

    public void tick(EntityPlayer player) {
        if (!open) return;
        if (input.consumeMousePress(0)) click(player, input.getMouseX(), input.getMouseY());
        if (input.consumeMousePress(1)) rightClick(player, input.getMouseX(), input.getMouseY());
    }

    private void click(EntityPlayer player, double mouseX, double mouseY) {
        Layout layout = new Layout(window.getWidth(), window.getHeight(), craftingTable);
        int slot = layout.playerSlot(mouseX, mouseY);
        if (slot >= 0) { swapMain(player, slot); return; }
        int craft = layout.craftSlot(mouseX, mouseY);
        if (craft >= 0) { swapCraft(player, craft); return; }
        if (layout.output(mouseX, mouseY)) craft(player);
    }

    private void rightClick(EntityPlayer player, double mouseX, double mouseY) {
        Layout layout = new Layout(window.getWidth(), window.getHeight(), craftingTable);
        int slot = layout.playerSlot(mouseX, mouseY);
        if (slot >= 0) {
            ItemStack stack = player.getInventory().getStackInSlot(slot);
            ItemStack cursor = player.getInventory().getCursorStack();
            if (cursor == null && stack != null && !stack.isEmpty()) {
                int take = (stack.getCount() + 1) / 2;
                player.getInventory().setCursorStack(new ItemStack(stack.getItem(), take, stack.getDamage()));
                stack.shrink(take);
                if (stack.isEmpty()) player.getInventory().setInventorySlotContents(slot, null);
            } else if (cursor != null && !cursor.isEmpty()) {
                if (stack == null || stack.isEmpty()) { player.getInventory().setInventorySlotContents(slot, new ItemStack(cursor.getItem(), 1, cursor.getDamage())); cursor.shrink(1); }
                else if (stack.isItemEqual(cursor) && stack.getCount() < stack.getMaxStackSize()) { stack.grow(1); cursor.shrink(1); }
            }
        }
    }

    private void swapMain(EntityPlayer player, int slot) {
        InventoryPlayer inventory = player.getInventory();
        ItemStack slotStack = inventory.getStackInSlot(slot);
        ItemStack cursor = inventory.getCursorStack();
        inventory.setInventorySlotContents(slot, cursor);
        inventory.setCursorStack(slotStack);
    }

    private void swapCraft(EntityPlayer player, int slot) {
        ItemStack[] grid = craftingTable ? player.getTableCraftingInventory() : player.getInventory().getCraftingInventory();
        ItemStack slotStack = grid[slot];
        ItemStack cursor = player.getInventory().getCursorStack();
        grid[slot] = cursor;
        player.getInventory().setCursorStack(slotStack);
    }

    private void craft(EntityPlayer player) {
        ItemStack[] grid = craftingTable ? player.getTableCraftingInventory() : player.getInventory().getCraftingInventory();
        int width = craftingTable ? 3 : 2;
        ItemStack result = CraftingManager.getInstance().findMatchingRecipe(grid, width, width);
        if (result != null && player.getInventory().canAddItem(result)) {
            player.getInventory().addItem(result);
            CraftingManager.getInstance().consumeIngredients(grid, width, width);
        }
    }

    public void render(EntityPlayer player) {
        ui.begin();
        if (open) renderInventory(player); else renderHud(player);
        ui.end();
    }

    private void renderHud(EntityPlayer player) {
        int width = window.getWidth(), height = window.getHeight();
        ui.rect(width / 2.0f - 1, height / 2.0f - 8, 2, 16, 0xFFFFFF, 0.85f);
        ui.rect(width / 2.0f - 8, height / 2.0f - 1, 16, 2, 0xFFFFFF, 0.85f);
        float hotbarX = width / 2.0f - 9 * 22 / 2.0f;
        float hotbarY = height - 48;
        ui.rect(hotbarX - 4, hotbarY - 4, 9 * 22 + 8, 38, 0x111111, 0.78f);
        for (int i = 0; i < 9; i++) {
            int color = i == player.getInventory().getCurrentItem() ? 0xE8D47A : 0x6E6E6E;
            ui.border(hotbarX + i * 22, hotbarY, 20, 30, i == player.getInventory().getCurrentItem() ? 2 : 1, color, 1);
            drawStack(player.getInventory().getStackInSlot(i), hotbarX + i * 22 + 4, hotbarY + 4, 12);
        }
        ui.text("HP " + (int) player.getHealth() + "/" + (int) player.getMaxHealth(), 12, height - 34, 2, 0xFFFFFF, 1);
        ItemStack held = player.getInventory().getCurrentStack();
        if (held != null && !held.isEmpty()) ui.text(held.getDisplayName(), 12, height - 18, 2, 0xFFFFFF, 0.85f);
    }

    private void renderInventory(EntityPlayer player) {
        int width = window.getWidth(), height = window.getHeight();
        ui.rect(0, 0, width, height, 0x000000, 0.45f);
        Layout layout = new Layout(width, height, craftingTable);
        ui.rect(layout.panelX, layout.panelY, layout.panelWidth, layout.panelHeight, 0x3B3B3B, 0.98f);
        ui.border(layout.panelX, layout.panelY, layout.panelWidth, layout.panelHeight, 3, 0x111111, 1);
        ui.text(craftingTable ? "CRAFTING" : "INVENTORY", layout.panelX + 14, layout.panelY + 14, 2, 0xFFFFFF, 1);
        for (int i = 0; i < (craftingTable ? 9 : 4); i++) {
            int x = i % layout.craftWidth, y = i / layout.craftWidth;
            drawSlot(layout.craftX + x * layout.slotSize, layout.craftY + y * layout.slotSize, craftingTable ? player.getTableCraftingStack(i) : player.getInventory().getCraftingStack(i), 0x202020);
        }
        drawSlot(layout.outputX, layout.outputY, CraftingManager.getInstance().findMatchingRecipe(craftingTable ? player.getTableCraftingInventory() : player.getInventory().getCraftingInventory(), layout.craftWidth, layout.craftWidth), 0x554B28);
        for (int row = 0; row < 3; row++) for (int col = 0; col < 9; col++) {
            int slot = 9 + row * 9 + col;
            drawSlot(layout.inventoryX + col * layout.slotSize, layout.inventoryY + row * layout.slotSize, player.getInventory().getStackInSlot(slot), 0x202020);
        }
        for (int col = 0; col < 9; col++) drawSlot(layout.inventoryX + col * layout.slotSize, layout.hotbarY, player.getInventory().getStackInSlot(col), 0x202020);
        ItemStack cursor = player.getInventory().getCursorStack();
        if (cursor != null && !cursor.isEmpty()) drawStack(cursor, (float) input.getMouseX() - 10, (float) input.getMouseY() - 10, 12);
    }

    private void drawSlot(float x, float y, ItemStack stack, int background) {
        ui.rect(x, y, 30, 30, background, 1);
        ui.border(x, y, 30, 30, 1, 0x8A8A8A, 1);
        drawStack(stack, x + 7, y + 7, 16);
    }

    private void drawStack(ItemStack stack, float x, float y, float size) {
        if (stack == null || stack.isEmpty() || stack.getItem() == null) return;
        Item item = stack.getItem();
        ui.rect(x, y, size, size, item.getColor(), 1);
        ui.border(x, y, size, size, 1, 0x111111, 1);
        if (stack.getCount() > 1) ui.text(String.valueOf(stack.getCount()), x + size - 8, y + size - 9, 1.4f, 0xFFFFFF, 1);
    }

    public void close() { ui.close(); }

    private static final class Layout {
        final int panelX, panelY, panelWidth = 380, panelHeight = 290, slotSize = 34;
        final int craftX, craftY, craftWidth, outputX, outputY, inventoryX, inventoryY, hotbarY;
        Layout(int width, int height, boolean table) {
            panelX = (width - panelWidth) / 2; panelY = (height - panelHeight) / 2;
            craftWidth = table ? 3 : 2;
            craftX = panelX + 38; craftY = panelY + 52;
            outputX = panelX + 190; outputY = panelY + 70;
            inventoryX = panelX + 38; inventoryY = panelY + 162; hotbarY = panelY + 260;
        }
        int playerSlot(double x, double y) {
            for (int row = 0; row < 3; row++) for (int col = 0; col < 9; col++) if (inside(x, y, inventoryX + col * slotSize, inventoryY + row * slotSize)) return 9 + row * 9 + col;
            for (int col = 0; col < 9; col++) if (inside(x, y, inventoryX + col * slotSize, hotbarY)) return col;
            return -1;
        }
        int craftSlot(double x, double y) {
            for (int row = 0; row < craftWidth; row++) for (int col = 0; col < craftWidth; col++) if (inside(x, y, craftX + col * slotSize, craftY + row * slotSize)) return row * craftWidth + col;
            return -1;
        }
        boolean output(double x, double y) { return inside(x, y, outputX, outputY); }
        private static boolean inside(double x, double y, int left, int top) { return x >= left && x < left + 30 && y >= top && y < top + 30; }
    }
}
