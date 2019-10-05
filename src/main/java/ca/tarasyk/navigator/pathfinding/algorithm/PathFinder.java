package ca.tarasyk.navigator.pathfinding.algorithm;

import ca.tarasyk.navigator.pathfinding.path.BlockPosPath;
import ca.tarasyk.navigator.pathfinding.goal.Goal;
import ca.tarasyk.navigator.pathfinding.node.PathNode;

import java.util.Optional;

public interface PathFinder {
    Optional<BlockPosPath> search(PathNode src, Goal goal);
}
