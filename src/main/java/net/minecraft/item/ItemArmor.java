package net.minecraft.item;

public final class ItemArmor extends Item {
    public enum ArmorSlot { HELMET, CHESTPLATE, LEGGINGS, BOOTS }
    private final ArmorSlot armorSlot;
    private final int armorPoints;

    public ItemArmor(int id, String name, ArmorSlot armorSlot, int armorPoints, int durability) {
        super(id, name, 1, durability);
        this.armorSlot = armorSlot;
        this.armorPoints = armorPoints;
    }
    public ArmorSlot getArmorSlot() { return armorSlot; }
    public int getArmorPoints() { return armorPoints; }
    @Override public int getColor() { return 0xD5D5D5; }
}
