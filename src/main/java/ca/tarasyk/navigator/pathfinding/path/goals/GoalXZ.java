package ca.tarasyk.navigator.pathfinding.path.goals;

import ca.tarasyk.navigator.BetterBlockPos;
import ca.tarasyk.navigator.pathfinding.algorithm.Heuristic;

public class GoalXZ extends Goal {
    public GoalXZ(BetterBlockPos pos) {
        super(pos);
    }

    @Override
    public boolean metGoal(BetterBlockPos pos) {
        return this.pos.getZ() == pos.getZ() && this.pos.getX() == pos.getX();
    }

    @Override
    public Double heuristic(BetterBlockPos pos) {
        return Heuristic.REALLY_FAST_HEURISTIC_XZ.apply(pos, this.pos);
    }
}
