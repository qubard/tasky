package ca.tarasyk.navigator.pathfinding.algorithm;

import ca.tarasyk.navigator.BetterBlockPos;
import ca.tarasyk.navigator.NavigatorMod;
import ca.tarasyk.navigator.NavigatorProvider;
import ca.tarasyk.navigator.pathfinding.path.BlockPosPath;
import ca.tarasyk.navigator.pathfinding.goals.Goal;
import ca.tarasyk.navigator.pathfinding.movement.Move;
import ca.tarasyk.navigator.pathfinding.util.PlayerUtil;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class PathRunner implements Runnable {

    private final BlockPosPath path;
    private Goal goal;

    public PathRunner(BlockPosPath path, Goal goal) {
        this.path = path;
        this.goal = goal;
    }

    @Override
    public void run() {
        List<BetterBlockPos> nodes = path.getNodes();

        EntityPlayer p = NavigatorProvider.getPlayer();

        int currIndex = path.indexForClosest(new BetterBlockPos(p.getPosition()), Heuristic.EUCLIDEAN_DISTANCE_3D);

        while (true) {
            p = NavigatorProvider.getPlayer();

            if (currIndex + 1 < nodes.size()) {
                BetterBlockPos prev = path.getNode(currIndex - 1);
                BetterBlockPos targetNode = path.getNode(currIndex + 1);
                BetterBlockPos nextNext = currIndex + 2 >= nodes.size() ? targetNode : path.getNode(currIndex + 2);

                // Moving diagonally, try to dig out the side blocks
                int adx = targetNode.getX() - nextNext.getX() > 0 ? 1 : targetNode.getX() - nextNext.getX() == 0 ? 0 : -1;
                int adz = targetNode.getZ() - nextNext.getZ() > 0 ? 1 :  targetNode.getZ() - nextNext.getZ() == 0 ? 0 : -1;
                if (adx != 0 && adz != 0) {
                    Move.digSideBlocks(prev, adx, adz);
                }

                double dx = p.posX - (targetNode.getX() + 0.5);
                double dz = p.posZ - (targetNode.getZ() + 0.5);
                float yaw = (float) (Math.atan2(dx, dz) * 180f / Math.PI);

                yaw = 180 - yaw;

                // Cheap "auto swim"
                if (NavigatorProvider.getPlayer().isInWater()) {
                    NavigatorProvider.getMinecraft().gameSettings.keyBindJump.setKeyBindState(NavigatorProvider.getMinecraft().gameSettings.keyBindJump.getKeyCode(), true);
                } else {
                    // Does the player have to jump up? (the target node is a block above)
                    PlayerUtil.jump(targetNode.getY() - (int)p.posY >= 1);
                }

                NavigatorProvider.getMinecraft().gameSettings.keyBindForward.setKeyBindState(NavigatorProvider.getMinecraft().gameSettings.keyBindForward.getKeyCode(), true);

                p.rotationYaw = yaw;

                if ((int) p.posY == targetNode.getY()) {
                    p.rotationPitch = 5f;
                } else if (p.posY > targetNode.getY()) {
                    p.rotationPitch = 20f;
                } else {
                    p.rotationPitch = -10f;
                }

                BetterBlockPos playerPos = new BetterBlockPos((int) (p.posX - 0.5), (int) p.posY, (int) (p.posZ - 0.5));
                int nextIndex = path.indexForClosest(new BetterBlockPos(playerPos), Heuristic.EUCLIDEAN_DISTANCE_3D);
                //NavigatorMod.printDebugMessage(nextIndex + ":" + (currIndex + 1) + ":"+nodes.size());
                if (nextIndex >= 0 && nextIndex >= currIndex){
                    currIndex = nextIndex;
                }
            } else {
                BetterBlockPos node = path.getNode(nodes.size() - 1);
                double dx = p.posX - (node.getX() + 0.5);
                double dz = p.posZ - (node.getZ() + 0.5);
                float yaw = (float) (Math.atan2(dx, dz) * 180f / Math.PI);

                yaw = 180 - yaw;
                p.rotationYaw = yaw;

                // goal.metGoal(new BetterBlockPos((int)(p.posX), (int)p.posY, (int)(p.posZ)))

                double a = p.posX < 0 ? Math.ceil(p.posX) : Math.floor(p.posX);
                double b = p.posZ < 0 ? Math.ceil(p.posZ) : Math.floor(p.posZ);
                double c = node.getX() + 0.5 < 0 ? Math.ceil(node.getX()+ 0.5 ) : Math.floor(node.getX()+ 0.5 );
                double d = node.getZ() + 0.5 < 0 ? Math.ceil(node.getZ()+ 0.5 ) : Math.floor(node.getZ()+ 0.5 );

                //NavigatorMod.printDebugMessage(p.posX +"," + p.posZ + "," + "->" + a + "," + b + "," + node.getX() + "," + node.getZ());

                if (a == c && b == d) {
                    NavigatorMod.printDebugMessage("at end");
                    break;
                }
            }
        }

        NavigatorMod.printDebugMessage("turned off forward");
        NavigatorProvider.getMinecraft().gameSettings.keyBindForward.setKeyBindState(NavigatorProvider.getMinecraft().gameSettings.keyBindForward.getKeyCode(), false);
        NavigatorProvider.getMinecraft().gameSettings.keyBindJump.setKeyBindState(NavigatorProvider.getMinecraft().gameSettings.keyBindJump.getKeyCode(), false);
    }
}
