package net.minecraft.util.math;

public enum Direction {
    DOWN,
    UP,
    NORTH,
    SOUTH,
    WEST,
    EAST;

    public static Direction[] valuesCached() {
        return values();
    }

    public Direction getOpposite() {
        return switch (this) {
            case DOWN -> UP;
            case UP -> DOWN;
            case NORTH -> SOUTH;
            case SOUTH -> NORTH;
            case WEST -> EAST;
            case EAST -> WEST;
        };
    }
}
