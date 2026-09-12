package net.minecraft.world;

import java.util.HashMap;
import java.util.Map;

public final class GameRules {
    private final Map<String, String> rules = new HashMap<String, String>();
    public GameRules() { rules.put("doMobSpawning", "true"); rules.put("keepInventory", "false"); rules.put("doDaylightCycle", "true"); }
    public boolean getBoolean(String key) { return Boolean.parseBoolean(rules.get(key)); }
    public void setOrCreateGameRule(String key, String value) { rules.put(key, value); }
    public String getString(String key) { return rules.containsKey(key) ? rules.get(key) : ""; }
}
