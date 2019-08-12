package ca.tarasyk.navigator.pathfinding.algorithm;

import ca.tarasyk.navigator.pathfinding.path.BlockPosPath;
import ca.tarasyk.navigator.pathfinding.goals.Goal;
import ca.tarasyk.navigator.pathfinding.node.PathNode;

import java.util.Optional;

abstract class PathFinder {

    protected Goal goal;

    public PathFinder(Goal goal) {
        this.goal = goal;
    }

    abstract Optional<BlockPosPath> search(PathNode src);
}
