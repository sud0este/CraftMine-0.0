package net.minecraft.entity.player;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRegistry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.item.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.RayTraceResult;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3;
import net.minecraft.client.input.Input;
import net.minecraft.world.World;

/** First-person player entity. Input is injected so the simulation remains independent of OpenGL. */
public final class EntityPlayer extends EntityLivingBase {
    private final Input input;
    private final InventoryPlayer inventory = new InventoryPlayer();
    private final ItemStack[] tableCraftingInventory = new ItemStack[9];
    private BlockPos diggingPosition;
    private Block diggingBlock;
    private float diggingProgress;
    private boolean guiOpen;
    private boolean craftingTableRequest;
    private boolean sprinting;
    private boolean sneaking;
    private int attackCooldown;

    public EntityPlayer(World world, Input input, double x, double y, double z) {
        super(world, 20.0f);
        this.input = input;
        setSize(0.6f, 1.8f);
        setPosition(x, y, z);
        inventory.setInventorySlotContents(0, new ItemStack(net.minecraft.item.ItemRegistry.WOODEN_PICKAXE, 1));
        inventory.setInventorySlotContents(1, new ItemStack(ItemRegistrySafe.planks(), 16));
        inventory.setInventorySlotContents(2, new ItemStack(net.minecraft.item.ItemRegistry.CRAFTING_TABLE, 1));
        inventory.setInventorySlotContents(3, new ItemStack(net.minecraft.item.ItemRegistry.TORCH, 16));
        inventory.setInventorySlotContents(9, new ItemStack(net.minecraft.item.ItemRegistry.DIRT, 32));
    }

    /* Avoid a static import cycle while ItemRegistry is bootstrapped by Main. */
    private static final class ItemRegistrySafe {
        static Item planks() { return net.minecraft.item.ItemRegistry.PLANKS; }
    }

    public InventoryPlayer getInventory() { return inventory; }
    public boolean isSprinting() { return sprinting; }
    public boolean isSneaking() { return sneaking; }
    public void setGuiOpen(boolean open) { guiOpen = open; if (open) { motionX = motionZ = 0; } }
    public boolean isGuiOpen() { return guiOpen; }
    public ItemStack getTableCraftingStack(int slot) { return slot < 0 || slot >= 9 ? null : tableCraftingInventory[slot]; }
    public void setTableCraftingStack(int slot, ItemStack stack) { if (slot >= 0 && slot < 9) tableCraftingInventory[slot] = stack == null || stack.isEmpty() ? null : stack; }
    public ItemStack[] getTableCraftingInventory() { return tableCraftingInventory; }
    public void requestCraftingTable() { craftingTableRequest = true; }
    public boolean consumeCraftingTableRequest() { boolean result = craftingTableRequest; craftingTableRequest = false; return result; }

    @Override public float getEyeHeight() { return 1.62f; }

    @Override public boolean attackEntityFrom(DamageSource source, float amount) {
        float reduction = Math.min(0.80f, inventory.getArmorValue() * 0.04f);
        return super.attackEntityFrom(source, amount * (1.0f - reduction));
    }

    @Override public void onUpdate() {
        super.prevPosX = posX; super.prevPosY = posY; super.prevPosZ = posZ;
        if (!isEntityAlive()) {
            health = maxHealth;
            setPosition(0.5, world.getTopSolidOrLiquidBlock(0, 0), 0.5);
            motionX = motionY = motionZ = 0;
            return;
        }
        if (attackCooldown > 0) attackCooldown--;
        if (guiOpen) {
            motionX = motionZ = 0;
            if (onGround) motionY = 0;
            return;
        }
        rotationYaw += input.consumeMouseDeltaX() * 0.15f;
        rotationPitch -= input.consumeMouseDeltaY() * 0.15f;
        if (rotationPitch > 89.0f) rotationPitch = 89.0f;
        if (rotationPitch < -89.0f) rotationPitch = -89.0f;
        int scroll = input.consumeScroll();
        if (scroll != 0) inventory.setCurrentItem((inventory.getCurrentItem() - scroll) % InventoryPlayer.HOTBAR_SIZE + InventoryPlayer.HOTBAR_SIZE);
        for (int number = 0; number < 9; number++) if (input.consumeKeyPress( GLFWKey.number(number) )) inventory.setCurrentItem(number);

        sneaking = input.isKeyDown(GLFWKey.SHIFT);
        sprinting = input.isKeyDown(GLFWKey.CONTROL) && input.isKeyDown(GLFWKey.W);
        Vec3 look = getLookVec();
        double forwardX = look.x, forwardZ = look.z;
        double rightX = -forwardZ, rightZ = forwardX;
        double moveX = 0, moveZ = 0;
        if (input.isKeyDown(GLFWKey.W)) { moveX += forwardX; moveZ += forwardZ; }
        if (input.isKeyDown(GLFWKey.S)) { moveX -= forwardX; moveZ -= forwardZ; }
        if (input.isKeyDown(GLFWKey.A)) { moveX += rightX; moveZ += rightZ; }
        if (input.isKeyDown(GLFWKey.D)) { moveX -= rightX; moveZ -= rightZ; }
        double length = Math.sqrt(moveX * moveX + moveZ * moveZ);
        if (length > 0.0) {
            double speed = sneaking ? 0.035 : (sprinting ? 0.115 : 0.075);
            motionX += moveX / length * speed;
            motionZ += moveZ / length * speed;
        }
        if (input.isKeyDown(GLFWKey.SPACE)) jump();
        if (input.isMouseDown(0)) mineOrAttack(); else resetDigging();
        if (input.consumeMousePress(1)) useItem();
        if (!noClip) motionY -= 0.08;
        move(motionX, motionY, motionZ);
        double friction = onGround ? (sneaking ? 0.45 : 0.60) : 0.91;
        motionX *= friction; motionZ *= friction; motionY *= 0.98;
    }

    private void mineOrAttack() {
        boolean pressed = input.consumeMousePress(0);
        EntityLivingBase target = findAttackTarget();
        RayTraceResult blockHit = world.rayTraceBlocks(getPositionEyes(1.0f), getPositionEyes(1.0f).add(getLookVec().scale(5.0)), false);
        if (target != null && pressed && attackCooldown == 0) {
            float damage = 1.0f;
            ItemStack held = inventory.getCurrentStack();
            if (held != null && held.getItem() instanceof ItemSword) damage = ((ItemSword) held.getItem()).getAttackDamage();
            target.attackEntityFrom(DamageSource.PLAYER, damage);
            inventory.damageCurrentItem(1);
            attackCooldown = 10;
            return;
        }
        if (blockHit.type != RayTraceResult.Type.BLOCK) return;
        Block block = world.getBlockState(blockHit.blockPos).getBlock();
        if (block == BlockRegistry.AIR || block.getHardness() < 0) return;
        if (!blockHit.blockPos.equals(diggingPosition) || block != diggingBlock) {
            diggingPosition = blockHit.blockPos;
            diggingBlock = block;
            diggingProgress = 0;
        }
        ItemStack held = inventory.getCurrentStack();
        float speed = held == null ? 1.0f : held.getDestroySpeed(block);
        boolean canHarvest = held != null && held.getItem().canHarvestBlock(held, block);
        if (held == null) canHarvest = block.getHardness() <= 0.6f;
        if (!canHarvest) speed *= 0.25f;
        diggingProgress += speed / Math.max(0.1f, block.getHardness()) / 20.0f;
        if (diggingProgress >= 1.0f) {
            world.setBlockState(diggingPosition, BlockRegistry.AIR.getDefaultState());
            if (canHarvest) {
                ItemStack[] drops = block.getDrops(world, diggingPosition, 0, held);
                for (ItemStack drop : drops) inventory.addItem(drop);
            }
            if (held != null && held.getItem() instanceof net.minecraft.item.ItemTool) inventory.damageCurrentItem(1);
            resetDigging();
        }
    }

    private EntityLivingBase findAttackTarget() {
        Vec3 start = getPositionEyes(1.0f);
        Vec3 look = getLookVec();
        Vec3 end = start.add(look.scale(4.2));
        AxisAlignedBB area = new AxisAlignedBB(Math.min(start.x, end.x), Math.min(start.y, end.y), Math.min(start.z, end.z),
                Math.max(start.x, end.x), Math.max(start.y, end.y), Math.max(start.z, end.z)).expand(0.8, 0.8, 0.8);
        List<EntityLivingBase> candidates = world.getEntitiesWithinAABB(EntityLivingBase.class, area, this);
        EntityLivingBase best = null;
        double bestDistance = Double.MAX_VALUE;
        for (EntityLivingBase candidate : candidates) {
            Vec3 center = new Vec3(candidate.posX, candidate.posY + candidate.getEyeHeight() * 0.5, candidate.posZ);
            Vec3 relative = center.subtract(start);
            double along = relative.dot(look);
            if (along < 0 || along > 4.2) continue;
            double lineDistance = relative.subtract(look.scale(along)).length();
            if (lineDistance <= candidate.getWidth() + 0.25 && along < bestDistance) { best = candidate; bestDistance = along; }
        }
        return best;
    }

    private void useItem() {
        Vec3 eye = getPositionEyes(1.0f);
        RayTraceResult hit = world.rayTraceBlocks(eye, eye.add(getLookVec().scale(5.0)), false);
        ItemStack held = inventory.getCurrentStack();
        if (hit.type == RayTraceResult.Type.BLOCK) {
            Block block = world.getBlockState(hit.blockPos).getBlock();
            if (block.onBlockActivated(world, hit.blockPos, this)) return;
            if (held != null && !held.isEmpty() && held.getItem() != null) held.getItem().onItemUse(held, this, world, hit.blockPos, hit.sideHit);
        } else if (held != null && held.getItem() instanceof ItemFood) {
            held.getItem().onItemRightClick(held, this, world);
        }
    }

    private void resetDigging() { diggingPosition = null; diggingBlock = null; diggingProgress = 0; }

    public float getDiggingProgress() { return diggingProgress; }
    public BlockPos getDiggingPosition() { return diggingPosition; }
    @Override public int getRenderColor() { return 0xE4A27A; }

    /** Constants avoid leaking GLFW through gameplay classes except this adapter. */
    private static final class GLFWKey {
        static final int W = 87, A = 65, S = 83, D = 68, SPACE = 32, SHIFT = 340, CONTROL = 341;
        static int number(int hotbar) { return 49 + hotbar; }
    }
}
