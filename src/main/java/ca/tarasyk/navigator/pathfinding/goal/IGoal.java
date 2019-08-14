package ca.tarasyk.navigator.pathfinding.goal;

import ca.tarasyk.navigator.BetterBlockPos;

public interface IGoal {
    /**
     * @param node The node we're checking against
     * @return Whether or not node meets the goal criteria
     */
    default boolean metGoal(BetterBlockPos pos) {
        return false;
    }

    /**
     * @param pos The position we want a heuristic against
     * @return The approximate cost from pos to the goal
     */
    default Double heuristic(BetterBlockPos pos) {
        return Double.MAX_VALUE;
    }
}
