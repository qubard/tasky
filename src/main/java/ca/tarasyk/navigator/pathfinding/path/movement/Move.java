package ca.tarasyk.navigator.pathfinding.path.movement;

import ca.tarasyk.navigator.BetterBlockPos;
import ca.tarasyk.navigator.pathfinding.path.node.PathNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Move {

    MOVE_POS_X(1, 0, 1), MOVE_NEG_X(-1, 0, 1),
    MOVE_POS_Y(0, 1, 0), MOVE_NEG_Y(0, -1, 0),
    MOVE_POS_Z(0, 0, 1), MOVE_NEG_Z(0, 0, -1),
    MOVE_POS_XZ(1, 0, 1), MOVE_NEGPOS_XZ(-1, 0, 1),
    MOVE_NEGNEG_XZ(-1, 0, -1), MOVE_POSNEG_XZ(1, 0, -1),
    MOVE_DOWN_POS_X(1, -1, 1), MOVE_DOWN_NEG_X(-1, -1, 1),
    MOVE_DOWN_POS_Z(0, -1, 1), MOVE_DOWN_NEG_Z(0, -1, -1),
    MOVE_DOWN_POS_XZ(1, -1, 1), MOVE_DOWN_NEGPOS_XZ(-1, -1, 1),
    MOVE_DOWN_NEGNEG_XZ(-1, -1, -1), MOVE_DOWN_POSNEG_XZ(1, -1, -1),
    MOVE_UP_POS_X(1, 1, 1), MOVE_UP_NEG_X(-1, 1, 1),
    MOVE_UP_POS_Z(0, 1, 1), MOVE_UP_NEG_Z(0, 1, -1),
    MOVE_UP_POS_XZ(1, 1, 1), MOVE_UP_NEGPOS_XZ(-1, 1, 1),
    MOVE_UP_NEGNEG_XZ(-1, 1, -1), MOVE_UP_POSNEG_XZ(1, 1, -1);

    private int dx, dy, dz;

    public static final ArrayList<Move> moves = new ArrayList<>(Arrays.asList(Move.values()));

    Move(int dx, int dy, int dz) {
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;
    }

    public PathNode apply(PathNode node) {
        BetterBlockPos pos = node.getPos();
        return new PathNode(pos.getX() + dx, pos.getY() + dy, pos.getZ() + dz);
    }

    public static List<PathNode> neighborsOf(PathNode node) {
        ArrayList<PathNode> neighbors = new ArrayList<>();
        for (Move move : Move.moves) {
            neighbors.add(move.apply(node));
        }
        return neighbors;
    }

}
