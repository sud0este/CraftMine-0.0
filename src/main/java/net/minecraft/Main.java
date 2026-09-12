package net.minecraft;

/** Compatibility entry point for people accustomed to the original launch class. */
public final class Main {
    private Main() { }
    public static void main(String[] args) { net.minecraft.client.Main.main(args); }
}
