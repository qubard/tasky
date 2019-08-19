package ca.tarasyk.navigator.pathfinding.path;

import ca.tarasyk.navigator.BetterBlockPos;
import ca.tarasyk.navigator.NavigatorMod;
import ca.tarasyk.navigator.NavigatorProvider;
import ca.tarasyk.navigator.pathfinding.algorithm.Heuristic;
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
            double a = p.posX < 0 ? Math.ceil(p.posX) : Math.floor(p.posX);
            double b = p.posZ < 0 ? Math.ceil(p.posZ) : Math.floor(p.posZ);
            double c = node.getX() + 0.5 < 0 ? Math.ceil(node.getX()+ 0.5 ) : Math.floor(node.getX()+ 0.5 );
            double d = node.getZ() + 0.5 < 0 ? Math.ceil(node.getZ()+ 0.5 ) : Math.floor(node.getZ()+ 0.5 );

            if (a == c && b == d) {
                return i;
            }
        }
        return minIndex;
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
