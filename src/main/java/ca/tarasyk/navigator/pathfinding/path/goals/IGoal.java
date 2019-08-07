package ca.tarasyk.navigator.pathfinding.path.goals;

import ca.tarasyk.navigator.pathfinding.path.node.PathNode;

public interface IGoal {
    /**
     * @param node The node we're checking against
     * @return Whether or not node meets the goal criteria
     */
    default boolean metGoal(PathNode node) {
        return false;
    }

    /**
     * @param node The node we're checking against
     * @return The approximate cost from node to the goal
     */
    default Double heuristic(PathNode node) {
        return Double.MAX_VALUE;
    }
}
