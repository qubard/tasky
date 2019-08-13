package ca.tarasyk.navigator.pathfinding.goals;

import ca.tarasyk.navigator.BetterBlockPos;
import ca.tarasyk.navigator.pathfinding.algorithm.Heuristic;

public class GoalXYZ extends Goal {
    public GoalXYZ(int x, int y, int z) {
        super(new BetterBlockPos(x, y, z));
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
