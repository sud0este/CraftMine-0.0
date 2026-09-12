package net.minecraft.block.material;

public enum Material {
    AIR(false, false),
    ROCK(true, true),
    EARTH(true, true),
    WOOD(true, true),
    LEAVES(true, false),
    SAND(true, true),
    GLASS(true, false),
    WATER(false, false),
    CLOTH(true, true),
    PLANTS(false, false);

    private final boolean blocksMovement;
    private final boolean opaque;

    Material(boolean blocksMovement, boolean opaque) {
        this.blocksMovement = blocksMovement;
        this.opaque = opaque;
    }

    public boolean blocksMovement() { return blocksMovement; }
    public boolean isOpaque() { return opaque; }
}
