package net.minecraft.registry.tag;

public final class TagKey<T> {
    private TagKey() {
    }

    public static <T> TagKey<T> of() {
        return new TagKey<>();
    }
}
