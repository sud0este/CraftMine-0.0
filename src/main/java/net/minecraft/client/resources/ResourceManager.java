package net.minecraft.client.resources;

import java.io.IOException;
import java.io.InputStream;

import net.minecraft.util.ResourceLocation;

/** Classpath resource access; official assets are intentionally never bundled. */
public final class ResourceManager {
    public InputStream open(ResourceLocation location) throws IOException {
        String path = "/assets/" + location.getDomain() + "/" + location.getPath();
        InputStream stream = ResourceManager.class.getResourceAsStream(path);
        if (stream == null) throw new IOException("Missing resource " + location);
        return stream;
    }
}
