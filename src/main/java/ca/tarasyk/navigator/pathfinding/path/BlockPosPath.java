package ca.tarasyk.navigator.pathfinding.path;

import ca.tarasyk.navigator.BetterBlockPos;
import ca.tarasyk.navigator.pathfinding.algorithm.Heuristic;

public class BlockPosPath extends Path<BetterBlockPos> {

    public BlockPosPath(Path<BetterBlockPos> path) {
        nodes = path.getNodes();
    }

    /**
     * @param count The number of nodes
     * @param pos The position
     * @return The path with `n` nodes near the given position
     */
    public Path<BetterBlockPos> pathNear(double range, BetterBlockPos pos) {
        Path<BetterBlockPos> newPath = new Path<>();
        for (BetterBlockPos node : nodes) {
            double d = Heuristic.REALLY_FAST_HEURISTIC_XZ.apply(node, pos);
            if (d <= range) {
                newPath.addNode(node);
            }
        }
        return newPath;
    }
}
