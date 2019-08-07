package ca.tarasyk.navigator.pathfinding.algorithm;

import ca.tarasyk.navigator.BetterBlockPos;
import ca.tarasyk.navigator.pathfinding.path.Path;
import ca.tarasyk.navigator.pathfinding.path.goals.Goal;
import ca.tarasyk.navigator.pathfinding.path.node.PathNode;

import java.util.Optional;

abstract class PathFinder {

    protected Goal goal;

    public PathFinder(Goal goal) {
        this.goal = goal;
    }

    abstract Optional<Path<BetterBlockPos>> search(PathNode src);
}
