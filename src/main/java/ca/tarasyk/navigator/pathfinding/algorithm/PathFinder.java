package ca.tarasyk.navigator.pathfinding.algorithm;

import ca.tarasyk.navigator.pathfinding.path.BlockPosPath;
import ca.tarasyk.navigator.pathfinding.goal.Goal;
import ca.tarasyk.navigator.pathfinding.node.PathNode;

import java.util.Optional;

abstract class PathFinder {

    protected boolean failed;

    abstract Optional<BlockPosPath> search(PathNode src, Goal goal);

    public boolean hasFailed() {
        return failed;
    }

    public void setFailed(boolean state) {
        this.failed = state;
    }
}
