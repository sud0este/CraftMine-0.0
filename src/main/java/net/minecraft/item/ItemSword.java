package net.minecraft.item;

import net.minecraft.block.Block;

public final class ItemSword extends Item {
    private final float attackDamage;
    public ItemSword(int id, String name, float attackDamage, int durability) {
        super(id, name, 1, durability);
        this.attackDamage = attackDamage;
    }
    public float getAttackDamage() { return attackDamage; }
    @Override public float getDestroySpeed(ItemStack stack, Block block) { return 1.5f; }
}
