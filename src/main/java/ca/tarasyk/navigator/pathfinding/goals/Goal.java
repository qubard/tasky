package ca.tarasyk.navigator.pathfinding.goals;

import ca.tarasyk.navigator.BetterBlockPos;

public abstract class Goal implements IGoal {

    protected BetterBlockPos pos;

    public Goal(BetterBlockPos pos) {
        this(pos.getX(), pos.getY(), pos.getZ());
    }

    public Goal(int x, int y, int z) {
        this.pos = new BetterBlockPos(x, y, z);
    }

    public BetterBlockPos getPos() {
        return pos;
    }
}
