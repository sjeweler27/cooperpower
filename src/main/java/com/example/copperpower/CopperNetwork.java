package com.example.copperpower;

import net.minecraft.block.BlockState;
import net.minecraft.block.Oxidizable;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public final class CopperNetwork {
    private static final Direction[] DIRECTIONS = Direction.values();

    private CopperNetwork() {
    }

    public static boolean isConductiveCopper(BlockState state) {
        return state != null && state.isIn(BlockTags.COPPER_BLOCKS);
    }

    public static int computePower(BlockState state, net.minecraft.world.BlockView world, BlockPos pos) {
        if (!isConductiveCopper(state)) {
            return 0;
        }

        if (!(world instanceof WorldAccess worldAccess)) {
            return 0;
        }

        Set<BlockPos> component = collectComponent(worldAccess, pos);
        if (component.isEmpty()) {
            return 0;
        }

        Map<BlockPos, Integer> insidePower = new HashMap<>();
        Deque<BlockPos> queue = new ArrayDeque<>();

        // Seed from external redstone sources touching the network
        for (BlockPos copperPos : component) {
            int best = insidePower.getOrDefault(copperPos, 0);
            for (Direction direction : DIRECTIONS) {
                BlockPos neighborPos = copperPos.offset(direction);
                if (component.contains(neighborPos)) {
                    continue;
                }
                int neighborPower = MathHelper.clamp(worldAccess.getEmittedRedstonePower(neighborPos, direction.getOpposite()), 0, 15);
                if (neighborPower > best) {
                    best = neighborPower;
                }
            }
            if (best > 0) {
                insidePower.put(copperPos, best);
                queue.addLast(copperPos);
            } else {
                insidePower.putIfAbsent(copperPos, 0);
            }
        }

        while (!queue.isEmpty()) {
            BlockPos current = queue.removeFirst();
            BlockState currentState = worldAccess.getBlockState(current);
            int inside = insidePower.getOrDefault(current, 0);
            int output = MathHelper.clamp(inside - getPenalty(currentState), 0, 15);
            if (output <= 0) {
                continue;
            }
            for (Direction direction : DIRECTIONS) {
                BlockPos neighbor = current.offset(direction);
                if (!component.contains(neighbor)) {
                    continue;
                }
                int previous = insidePower.getOrDefault(neighbor, 0);
                if (output > previous) {
                    insidePower.put(neighbor, output);
                    queue.addLast(neighbor);
                }
            }
        }

        int startInside = insidePower.getOrDefault(pos, 0);
        int finalPower = MathHelper.clamp(startInside - getPenalty(state), 0, 15);
        return finalPower;
    }

    private static Set<BlockPos> collectComponent(WorldView world, BlockPos origin) {
        Set<BlockPos> component = new HashSet<>();
        Deque<BlockPos> stack = new ArrayDeque<>();
        stack.push(origin);
        component.add(origin);

        while (!stack.isEmpty()) {
            BlockPos current = stack.pop();
            for (Direction direction : DIRECTIONS) {
                BlockPos neighbor = current.offset(direction);
                BlockState neighborState = world.getBlockState(neighbor);
                if (isConductiveCopper(neighborState) && component.add(neighbor)) {
                    stack.push(neighbor);
                }
            }
        }

        return component;
    }

    private static int getPenalty(BlockState state) {
        Optional<Oxidizable.OxidationLevel> levelOptional = Oxidizable.getOxidationLevel(state);
        if (levelOptional.isEmpty()) {
            return 0;
        }

        return switch (levelOptional.get()) {
            case UNAFFECTED -> 0;
            case EXPOSED -> 1;
            case WEATHERED -> 2;
            case OXIDIZED -> 3;
        };
    }
}
