package ca.tarasyk.navigator.pathfinding.path.goals;

import ca.tarasyk.navigator.BetterBlockPos;
import ca.tarasyk.navigator.pathfinding.algorithm.Heuristic;

public class SimpleGoal extends Goal {
    public SimpleGoal(BetterBlockPos pos) {
        super(pos);
    }

    @Override
    public boolean metGoal(BetterBlockPos pos) {
        return this.pos.equals(pos);
    }

    @Override
    public Double heuristic(BetterBlockPos pos) {
        return Heuristic.REALLY_FAST_HEURISTIC.apply(pos, this.pos);
    }
}
