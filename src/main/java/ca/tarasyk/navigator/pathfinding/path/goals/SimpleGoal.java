package ca.tarasyk.navigator.pathfinding.path.goals;

import ca.tarasyk.navigator.BetterBlockPos;
import ca.tarasyk.navigator.pathfinding.algorithm.Heuristic;
import ca.tarasyk.navigator.pathfinding.path.node.PathNode;

public class SimpleGoal extends Goal {
    public SimpleGoal(BetterBlockPos pos) {
        super(pos);
    }

    @Override
    public boolean metGoal(PathNode node) {
        return node.getPos().equals(pos);
    }

    @Override
    public Double heuristic(PathNode node) {
        return Heuristic.BLOCKNODE_EUCLIDEAN_DISTANCE.apply(node.getPos(), pos);
    }
}
