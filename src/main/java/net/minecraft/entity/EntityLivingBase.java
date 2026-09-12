package net.minecraft.entity;

import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityLivingBase extends Entity {
    protected float health;
    protected float maxHealth;
    protected int hurtTime;
    protected int deathTime;
    protected int attackTime;

    public EntityLivingBase(World world, float maxHealth) {
        super(world);
        this.maxHealth = maxHealth;
        this.health = maxHealth;
    }

    public float getHealth() { return health; }
    public float getMaxHealth() { return maxHealth; }
    public void heal(float amount) { health = Math.min(maxHealth, health + amount); }
    public boolean isEntityAlive() { return !dead && health > 0.0f; }

    @Override public void onUpdate() {
        super.onUpdate();
        if (!isEntityAlive()) { if (++deathTime > 20) setDead(); return; }
        if (hurtTime > 0) hurtTime--;
        if (attackTime > 0) attackTime--;
        onLivingUpdate();
        if (!noClip) motionY -= 0.08;
        move(motionX, motionY, motionZ);
        double friction = onGround ? 0.60 : 0.91;
        motionX *= friction;
        motionZ *= friction;
        motionY *= 0.98;
    }

    protected void onLivingUpdate() { }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (!isEntityAlive() || hurtTime > 0) return false;
        health -= amount;
        hurtTime = 10;
        if (health <= 0.0f) onDeath(source);
        return true;
    }

    protected void onDeath(DamageSource source) { }
    public void jump() { if (onGround) motionY = 0.42; }
    public boolean canEntityBeSeen(Entity entity) { return true; }
}
