package ca.tarasyk.navigator.pathfinding.path;

import ca.tarasyk.navigator.BetterBlockPos;
import ca.tarasyk.navigator.NavigatorMod;
import ca.tarasyk.navigator.NavigatorProvider;
import ca.tarasyk.navigator.pathfinding.algorithm.Heuristic;
import ca.tarasyk.navigator.pathfinding.util.PlayerUtil;
import net.minecraft.entity.player.EntityPlayer;

import java.util.function.BiFunction;

public class BlockPosPath extends Path<BetterBlockPos> {

    public BlockPosPath(Path<BetterBlockPos> path) {
        super(path.reversed);
        nodes = path.getNodes();
    }

    /**
     * @param start The start index
     * @return `start` + 1 or start (whichever is closer to the player)
     */
    public int nextClosest(int start) {
        int minIndex = start;
        for (int i = start; i < Math.min(start + 3, nodes.size()); i++) {
            BetterBlockPos node = getNode(i);
            EntityPlayer p = NavigatorProvider.getPlayer();
            double x1 = p.posX < 0 ? Math.ceil(p.posX) : Math.floor(p.posX);
            double z1 = p.posZ < 0 ? Math.ceil(p.posZ) : Math.floor(p.posZ);
            double x2 = node.getX() + 0.5 < 0 ? Math.ceil(node.getX() + 0.5) : Math.floor(node.getX() + 0.5);
            double z2 = node.getZ() + 0.5 < 0 ? Math.ceil(node.getZ() + 0.5) : Math.floor(node.getZ() + 0.5);

            if (x1 == x2 && z1 == z2) {
                return i;
            }
        }
        return minIndex;
    }


    /**
     * @param index The node's index
     * @return The yaw rotation (direction vector) for the node in the path
     */
    public float computeYawForNode(int index, float def) {
        if (nodes.size() == 1) {
            return def;
        }

        if (index < 0) {
            return computeYawForNode(0, def);
        }

        if (index >= nodes.size() - 1) {
            return computeYawForNode(nodes.size() - 2, def);
        }

        EntityPlayer player = NavigatorProvider.getPlayer();
        BetterBlockPos curr = getNode(index);
        BetterBlockPos next = getNode(index + 1);
        return PlayerUtil.yawFrom(player.posX, player.posZ, next,
                next.getX() - curr.getX(), next.getZ() - curr.getZ());
    }

    /**
     * @param range The range from pos
     * @param pos The position
     * @return The path nodes near `pos` at distance `range`
     */
    public Path<BetterBlockPos> pathNear(double range, BetterBlockPos pos) {
        Path<BetterBlockPos> newPath = new Path<>(false);
        for (BetterBlockPos node : nodes) {
            double d = Heuristic.EUCLIDEAN_DISTANCE_2D.apply(node, pos);
            if (d <= range) {
                newPath.pushNode(node);
            }
        }
        return newPath;
    }
}
