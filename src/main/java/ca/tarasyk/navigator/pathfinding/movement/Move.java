package ca.tarasyk.navigator.pathfinding.movement;

import ca.tarasyk.navigator.BetterBlockPos;
import ca.tarasyk.navigator.NavigatorProvider;
import ca.tarasyk.navigator.pathfinding.algorithm.Heuristic;
import ca.tarasyk.navigator.pathfinding.node.PathNode;
import net.minecraft.block.Block;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

import java.util.*;

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

    private static void lookAt(BetterBlockPos pos) {
        EntityPlayer p = NavigatorProvider.getPlayer();

        double dx = p.posX - (double) (pos.getX() + 0.5);
        double dy = p.posY - (double) (pos.getY() + 0.5);
        double dz = p.posZ - (double) (pos.getZ() + 0.5);
        float yaw = (float) (Math.atan2(dx, dz) * 180f / Math.PI);
        yaw = 180 - yaw;
        p.rotationYaw = yaw;
        if ((int) p.posY > pos.getY()) {
            p.rotationPitch = 90f;
        } else {
            p.rotationPitch = 45f;
        }
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
        return !block.isPassable(ctx, pos) || (block == Blocks.LAVA || block == Blocks.FLOWING_LAVA || block == Blocks.FLOWING_WATER || block == Blocks.LEAVES || block == Blocks.LEAVES2);
    }

    public static boolean isStrictlySolid(WorldClient ctx, BlockPos pos) {
        Block block = ctx.getBlockState(pos).getBlock();
        return !block.isPassable(ctx, pos);
    }

    public static void digSideBlocks(BetterBlockPos pos, int dx, int dz) {
        BetterBlockPos left = new BetterBlockPos(dx, 0, 0);
        BetterBlockPos right = new BetterBlockPos(0, 0, dz);
        BetterBlockPos leftUp = left.up();
        BetterBlockPos rightUp = right.up();

        Queue<BetterBlockPos> toRemove = new LinkedList<>();

        toRemove.add(pos.add(left));
        toRemove.add(pos.add(right));
        toRemove.add(pos.add(leftUp));
        toRemove.add(pos.add(rightUp));

        while (!toRemove.isEmpty()) {
            BetterBlockPos remove = toRemove.peek();
            if (Move.isStrictlySolid(NavigatorProvider.getWorld(), remove)) {
                NavigatorProvider.getMinecraft().gameSettings.keyBindForward.setKeyBindState(NavigatorProvider.getMinecraft().gameSettings.keyBindForward.getKeyCode(), false);
                lookAt(remove);
                NavigatorProvider.getMinecraft().playerController.onPlayerDamageBlock(remove, EnumFacing.UP);
                NavigatorProvider.getMinecraft().effectRenderer.addBlockHitEffects(remove, EnumFacing.UP);
                NavigatorProvider.getMinecraft().player.swingArm(EnumHand.MAIN_HAND);
                try {
                    Thread.sleep(50);
                } catch (Exception e) {

                }
            } else {
                // Right click the block to make sure its gone,
                /*NavigatorProvider.getMinecraft().playerController.processRightClickBlock(NavigatorProvider.getMinecraft().player, NavigatorProvider.getWorld(),
                        remove, EnumFacing.DOWN, NavigatorProvider.getMinecraft().player.getLookVec(),
                        EnumHand.MAIN_HAND);
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {

                }*/
                toRemove.remove();
            }
        }
    }

    /**
     * @param ctx The in-game World
     * @param src The source path node
     * @param dest The destination path node
     * @return The cost of a move from `src` to `dest`
     */
    public static Optional<Double> calculateCost(WorldClient ctx, PathNode src, PathNode dest) {
        int dy = dest.getPos().getY() - src.getPos().getY();
        boolean NOT_CLIMBING_OR_ASCENDING = dy == 0;
        boolean movingXZ = dest.getPos().getX() - src.getPos().getZ() != 0 || dest.getPos().getZ() - src.getPos().getZ() != 0;

        if (dest.getPos().getZ() - src.getPos().getZ() != 0 && dest.getPos().getX() - src.getPos().getX() != 0) {
            return Optional.ofNullable(null);
        }

        double totalCost = Heuristic.REALLY_FAST_HEURISTIC_XZ.apply(src.getPos(), dest.getPos());

        // Trying to climb but not pillaring straight up, increases search space complexity
        if (!isSolid(ctx, dest.getPos().down())) {
            // Nothing to stand on, or unloaded chunk, dest block is solid (can't jump on it), or we have to dig to get to the block
            return Optional.ofNullable(null);
        }

        if (isSolid(ctx, dest.getPos())) {
            return Optional.ofNullable(null);
        }

        if (NOT_CLIMBING_OR_ASCENDING) {
            // A block is blocking the way we can mark this as diggable though later and dig it out.. or avoid it here
            // We can assume src is always walkable here (by at least 2 blocks)
            if (isSolid(ctx, dest.getPos().up())) {
                return Optional.ofNullable(null);
            }
        }

        // We're moving up or down
        if (dy > 0) { // Moving up
            // We have to dig out one of these blocks (head will hit the block), but here we just ignore it
            if (isSolid(ctx, src.getPos().up().up()) || isSolid(ctx, dest.getPos().up())) {
                return Optional.ofNullable(null);
            }
        } else {
            if (isSolid(ctx, dest.getPos().up().up()) || isSolid(ctx, dest.getPos().up())) {
                return Optional.ofNullable(null);
            }
        }

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
