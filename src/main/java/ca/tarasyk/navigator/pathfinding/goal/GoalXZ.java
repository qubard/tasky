package ca.tarasyk.navigator.pathfinding.goal;

import ca.tarasyk.navigator.BetterBlockPos;
import ca.tarasyk.navigator.pathfinding.algorithm.Heuristic;

public class GoalXZ extends Goal {
    public GoalXZ(int x, int z) {
        super(new BetterBlockPos(x, z));
    }

    @Override
    public boolean metGoal(BetterBlockPos pos) {
        return pos.getX() == this.getPos().getX() && pos.getZ() == this.getPos().getZ();
    }

    @Override
    public Double heuristic(BetterBlockPos pos) {
        return Heuristic.REALLY_FAST_HEURISTIC.apply(pos, this.pos);
    }
}
