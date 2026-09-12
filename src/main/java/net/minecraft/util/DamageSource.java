package net.minecraft.util;

public final class DamageSource {
    public static final DamageSource GENERIC = new DamageSource("generic");
    public static final DamageSource MOB = new DamageSource("mob");
    public static final DamageSource PLAYER = new DamageSource("player");
    public static final DamageSource FALL = new DamageSource("fall");

    private final String damageType;

    public DamageSource(String damageType) { this.damageType = damageType; }
    public String getDamageType() { return damageType; }
}
