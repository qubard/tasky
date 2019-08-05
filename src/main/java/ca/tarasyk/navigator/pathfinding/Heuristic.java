package ca.tarasyk.navigator.pathfinding;

import ca.tarasyk.navigator.pathfinding.path.node.PathNode;

import java.util.function.BiFunction;

public class Heuristic {
    public static BiFunction<PathNode, PathNode, Double> BLOCKNODE_EUCLIDEAN_DISTANCE = (src, dst) -> {
        double dx = dst.getPos().getX() - src.getPos().getX();
        double dy = dst.getPos().getY() - src.getPos().getY();
        double dz = dst.getPos().getZ() - src.getPos().getZ();
        return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2) + Math.pow(dz, 2));
    };
}
