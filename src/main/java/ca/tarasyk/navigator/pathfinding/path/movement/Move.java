package ca.tarasyk.navigator.pathfinding.path.movement;

import ca.tarasyk.navigator.BetterBlockPos;
import ca.tarasyk.navigator.pathfinding.algorithm.Heuristic;
import ca.tarasyk.navigator.pathfinding.path.node.PathNode;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public enum Move {

    // "I wouldn't hire anyone who wrote out all of these enums" - Kim Tran
    MOVE_POS_X(1, 0, 0), MOVE_NEG_X(-1, 0, 0),
    MOVE_POS_Z(0, 0, 1), MOVE_NEG_Z(0, 0, -1),
    MOVE_POSPOS_XZ(1, 0, 1), MOVE_NEGPOS_XZ(-1, 0, 1),
    MOVE_NEGNEG_XZ(-1, 0, -1), MOVE_POSNEG_XZ(1, 0, -1),
    MOVE_DOWN_POS_X(1, -1, 0), MOVE_DOWN_NEG_X(-1, -1, 0),
    MOVE_DOWN_POS_Z(0, -1, 1), MOVE_DOWN_NEG_Z(0, -1, -1),
    MOVE_DOWN_POS_XZ(1, -1, 1), MOVE_DOWN_NEGPOS_XZ(-1, -1, 1),
    MOVE_DOWN_NEGNEG_XZ(-1, -1, -1), MOVE_DOWN_POSNEG_XZ(1, -1, -1),
    MOVE_UP_POS_X(1, 1, 0), MOVE_UP_NEG_X(-1, 1, 0),
    MOVE_UP_POS_Z(0, 1, 1), MOVE_UP_NEG_Z(0, 1, -1),
    MOVE_UP_POS_XZ(1, 1, 1), MOVE_UP_NEGPOS_XZ(-1, 1, 1),
    MOVE_UP_NEGNEG_XZ(-1, 1, -1), MOVE_UP_POSNEG_XZ(1, 1, -1),
    MOVE_POS_Y(0, 1, 0), MOVE_NEG_Y(0, -1, 0);

    private int dx, dy, dz;

    public static final ArrayList<Move> moves = new ArrayList<>(Arrays.asList(Move.values()));

    Move(int dx, int dy, int dz) {
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;
    }

    /**
     * @param prev The node we're checking against
     * @return A new PathNode with this move applied to it
     */
    public PathNode apply(PathNode prev) {
        BetterBlockPos pos = prev.getPos();
        return new PathNode(pos.getX() + dx, pos.getY() + dy, pos.getZ() + dz);
    }

    /**
     * @param ctx The in-game World
     * @param pos The position
     * @return Whether or not the block at `pos` is solid
     */
    public static boolean isSolid(WorldClient ctx, BlockPos pos) {
        Block block = ctx.getBlockState(pos).getBlock();
        return !block.isPassable(ctx, pos) || (block == Blocks.LAVA || block == Blocks.FLOWING_LAVA || block == Blocks.FLOWING_WATER);
    }

    /**
     * @param ctx The in-game World
     * @param src The source path node
     * @param dest The destination path node
     * @return The cost of a move from `src` to `dest`
     */
    public static Optional<Double> calculateCost(WorldClient ctx, PathNode src, PathNode dest) {
        boolean NOT_CLIMBING_OR_ASCENDING = dest.getPos().getY() - src.getPos().getY() == 0;
        boolean movingXZ = dest.getPos().getX() - src.getPos().getZ() != 0 || dest.getPos().getZ() - src.getPos().getZ() != 0;

        double totalCost = Heuristic.REALLY_FAST_HEURISTIC_XZ.apply(src.getPos(), dest.getPos());

        // Trying to climb but not pillaring straight up, increases search space complexity
        if (!isSolid(ctx, dest.getPos().down()) && movingXZ) {
            // Nothing to stand on, or unloaded chunk, dest block is solid (can't jump on it), or we have to dig to get to the block
            return Optional.ofNullable(null);
        }

        if (isSolid(ctx, dest.getPos())) {
            //totalCost += CostUtil.digCost(NavigatorProvider.getPlayer().inventory, ctx.getBlockState(dest.getPos()));
            return Optional.ofNullable(null);
        }

        if (NOT_CLIMBING_OR_ASCENDING) {
            // A block is blocking the way
            if (isSolid(ctx, dest.getPos().up())) {
                //totalCost += CostUtil.digCost(NavigatorProvider.getPlayer().inventory, ctx.getBlockState(dest.getPos().up()));
                return Optional.ofNullable(null);
            }
        }

        // It simply costs the movement cost in ticks to move there
        // Player moves at 0.14 blocks per tick max, but we want to estimate # of ticks
        return Optional.of(totalCost);
    }

    /**
     * @param node The node we're looking at
     * @return The neighbors of `node`
     */
    public static List<PathNode> neighborsOf(PathNode node) {
        ArrayList<PathNode> neighbors = new ArrayList<>();
        for (Move move : Move.moves) {
            neighbors.add(move.apply(node));
        }
        return neighbors;
    }
}
