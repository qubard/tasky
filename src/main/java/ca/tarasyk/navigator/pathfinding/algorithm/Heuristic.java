package ca.tarasyk.navigator.pathfinding.algorithm;

import ca.tarasyk.navigator.BetterBlockPos;

import java.util.function.BiFunction;

public class Heuristic {
    public static BiFunction<BetterBlockPos, BetterBlockPos, Double> BLOCKNODE_EUCLIDEAN_DISTANCE = (src, dst) -> {
        double dx = dst.getX() - src.getX();
        double dy = dst.getY() - src.getY();
        double dz = dst.getZ() - src.getZ();
        return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2) + Math.pow(dz, 2));
    };
}
