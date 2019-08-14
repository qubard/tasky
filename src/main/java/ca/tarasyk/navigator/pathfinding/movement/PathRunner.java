package ca.tarasyk.navigator.pathfinding.movement;

import ca.tarasyk.navigator.BetterBlockPos;
import ca.tarasyk.navigator.NavigatorMod;
import ca.tarasyk.navigator.NavigatorProvider;
import ca.tarasyk.navigator.pathfinding.path.BlockPosPath;
import ca.tarasyk.navigator.pathfinding.goal.Goal;
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

        int currIndex = path.nextClosest(0);
        long elapsedMoveTime = 0; // So we don't get stuck at a node (a little bit unpreventable)

        while (true) {
            if (currIndex + 1 < nodes.size()) {
                BetterBlockPos targetNode = path.getNode(currIndex + 1);

                PlayerUtil.lookAtXZ(targetNode);

                int nextIndex = path.nextClosest(currIndex);

                // Does the player have to jump up? (the target node is a block above), or in water
                PlayerUtil.moveForward();
                PlayerUtil.jump((targetNode.getY() - (int)p.posY >= 1) || NavigatorProvider.getPlayer().isInWater());

                if ((nextIndex >= 0 && nextIndex > currIndex)){
                    currIndex = nextIndex;
                    elapsedMoveTime = System.currentTimeMillis();
                } else if (PlayerUtil.playerAt(targetNode) && (System.currentTimeMillis() - elapsedMoveTime) >= 300) {
                    currIndex++;
                }
            } else {
                BetterBlockPos node = path.getNode(nodes.size() - 1);
                double dx = p.posX - (node.getX() + 0.5);
                double dz = p.posZ - (node.getZ() + 0.5);
                float yaw = (float) (Math.atan2(dx, dz) * 180f / Math.PI);

                yaw = 180 - yaw;
                p.rotationYaw = yaw;

                // goal.metGoal(new BetterBlockPos((int)(p.posX), (int)p.posY, (int)(p.posZ)))

                if (PlayerUtil.playerAt(node)) {
                    PlayerUtil.stopMovingForward();
                    break;
                }
            }
        }

        NavigatorMod.printDebugMessage("turned off forward");
        NavigatorProvider.getMinecraft().gameSettings.keyBindForward.setKeyBindState(NavigatorProvider.getMinecraft().gameSettings.keyBindForward.getKeyCode(), false);
        NavigatorProvider.getMinecraft().gameSettings.keyBindJump.setKeyBindState(NavigatorProvider.getMinecraft().gameSettings.keyBindJump.getKeyCode(), false);
    }
}
