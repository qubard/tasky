package ca.tarasyk.navigator.pathfinding.goal;

import ca.tarasyk.navigator.BetterBlockPos;
import ca.tarasyk.navigator.pathfinding.node.PathNode;

public abstract class Goal implements PathGoal {
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

    public PathNode toPathNode() {
        return new PathNode(pos);
    }

    @Override
    public String toString() {
        return pos.toString();
    }
}
