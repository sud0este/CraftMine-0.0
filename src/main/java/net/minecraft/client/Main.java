package net.minecraft.client;

public final class Main {
    private Main() { }
    public static void main(String[] args) {
        long seed = System.currentTimeMillis();
        if (args.length > 0) {
            try { seed = Long.parseLong(args[0]); }
            catch (NumberFormatException ignored) { System.err.println("Seed inválida; usando relógio do sistema."); }
        }
        new Minecraft(seed).run();
    }
}
