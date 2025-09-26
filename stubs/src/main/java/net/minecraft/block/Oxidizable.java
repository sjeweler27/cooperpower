package net.minecraft.block;

import java.util.Optional;

public interface Oxidizable {
    static Optional<OxidationLevel> getOxidationLevel(BlockState state) {
        return Optional.empty();
    }

    enum OxidationLevel {
        UNAFFECTED,
        EXPOSED,
        WEATHERED,
        OXIDIZED
    }
}
