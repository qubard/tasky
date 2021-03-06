package ca.tarasyk.navigator.pathfinding.node;

import ca.tarasyk.navigator.BetterBlockPos;
import ca.tarasyk.navigator.pathfinding.algorithm.score.AStarScore;

import java.util.Optional;

public class PathNode extends Node<AStarScore, BetterBlockPos> {

    private BetterBlockPos pos;

    public PathNode(int x, int y, int z) {
        this(new BetterBlockPos(x, y, z));
    }

    public PathNode(BetterBlockPos pos) {
        this.pos = pos;
        this.setScore((new AStarScore()).defaultValue());
    }

    @Override
    public int hashCode() {
        return this.pos.hashCode();
    }

    public BetterBlockPos getPos() {
        return this.pos;
    }

    @Override
    public Optional<BetterBlockPos> getData() {
        return Optional.of(getPos());
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PathNode)) {
            return false;
        }

        return ((PathNode) o).getPos().equals(this.getPos());
    }
}
