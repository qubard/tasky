package ca.tarasyk.navigator.pathfinding.algorithm;

import ca.tarasyk.navigator.BetterBlockPos;
import ca.tarasyk.navigator.pathfinding.movement.MoveConstants;

import java.util.function.BiFunction;

public class Heuristic {
    public static BiFunction<BetterBlockPos, BetterBlockPos, Double> REALLY_FAST_HEURISTIC = (src, dst) -> {
        double dx = dst.getX() - src.getX();
        double dy = dst.getY() - src.getY();
        double dz = dst.getZ() - src.getZ();
        return MoveConstants.MIN_ADMISSIBLE_DISTANCE * (Math.abs(dx) + Math.abs(dy) + Math.abs(dz));
    };

    public static BiFunction<BetterBlockPos, BetterBlockPos, Double> REALLY_FAST_HEURISTIC_XZ = (src, dst) -> {
        double dx = dst.getX() - src.getX();
        double dz = dst.getZ() - src.getZ();
        return MoveConstants.MIN_ADMISSIBLE_DISTANCE * (Math.abs(dx) + Math.abs(dz));
    };

    public static BiFunction<BetterBlockPos, BetterBlockPos, Double> EUCLIDEAN_DISTANCE_2D = (src, dst) -> {
        double dx = dst.getX() - src.getX();
        double dz = dst.getZ() - src.getZ();
        return Math.sqrt(Math.pow(dx, 2) + Math.pow(dz, 2));
    };

    public static BiFunction<BetterBlockPos, BetterBlockPos, Double> EUCLIDEAN_DISTANCE_3D = (src, dst) -> {
        double dx = dst.getX() - src.getX();
        double dy = dst.getY() - src.getY();
        double dz = dst.getZ() - src.getZ();
        return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2) + Math.pow(dz, 2));
    };
}
