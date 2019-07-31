package ca.tarasyk.navigator.pathfinding.path.node;

import ca.tarasyk.navigator.BetterBlockPos;
import ca.tarasyk.navigator.pathfinding.AStarScore;

public class BlockNode extends Node<BlockNode, AStarScore<Double>> {

    private BetterBlockPos pos;

    public BlockNode(int x, int y, int z) {
        this(new BetterBlockPos(x, y, z));
    }

    BlockNode(BetterBlockPos pos) {
        this.pos = pos;
    }

    @Override
    public int hashCode() {
        return this.pos.hashCode();
    }

    public BetterBlockPos getPos() {
        return this.pos;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BlockNode)) {
            return false;
        }

        return ((BlockNode) o).getPos().equals(this.getPos());
    }
}
