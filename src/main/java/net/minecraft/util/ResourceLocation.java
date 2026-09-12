package net.minecraft.util;

public final class ResourceLocation {
    private final String domain;
    private final String path;

    public ResourceLocation(String location) {
        int separator = location.indexOf(':');
        if (separator < 0) {
            domain = "craftmine";
            path = location;
        } else {
            domain = location.substring(0, separator);
            path = location.substring(separator + 1);
        }
    }

    public ResourceLocation(String domain, String path) {
        this.domain = domain;
        this.path = path;
    }

    public String getDomain() { return domain; }
    public String getPath() { return path; }

    @Override
    public String toString() { return domain + ':' + path; }

    @Override
    public boolean equals(Object object) {
        return object instanceof ResourceLocation && toString().equals(object.toString());
    }

    @Override
    public int hashCode() { return toString().hashCode(); }
}
