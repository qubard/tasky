package ca.tarasyk.navigator.pathfinding.path;

import ca.tarasyk.navigator.BetterBlockPos;
import ca.tarasyk.navigator.pathfinding.algorithm.Heuristic;

import java.util.function.BiFunction;

public class BlockPosPath extends Path<BetterBlockPos> {

    public BlockPosPath(Path<BetterBlockPos> path) {
        nodes = path.getNodes();
    }

    /**
     * @param pos
     * @return The index of the closest node to `pos` using the given heuristic
     */
    public int indexForClosest(BetterBlockPos pos, BiFunction<BetterBlockPos, BetterBlockPos, Double> heuristic) {
        int minIndex = 0;
        double minD = Double.MAX_VALUE;
        // we can't really do this in logn time unless we use a quadtree
        for (int i = 0; i < nodes.size(); i++) {
            BetterBlockPos node = getNode(i);
            double d = heuristic.apply(new BetterBlockPos(node.getX() + 0.5, pos.getY(), node.getZ() + 0.5), pos);
            if (d < minD) {
                minIndex = i;
                minD = d;
            }
        }
        return minIndex;
    }

    /**
     * @param count The number of nodes
     * @param pos The position
     * @return The path with `n` nodes near the given position
     */
    public Path<BetterBlockPos> pathNear(double range, BetterBlockPos pos) {
        Path<BetterBlockPos> newPath = new Path<>();
        for (BetterBlockPos node : nodes) {
            double d = Heuristic.EUCLIDEAN_DISTANCE_2D.apply(node, pos);
            if (d <= range) {
                newPath.addNode(node);
            }
        }
        return newPath;
    }
}
