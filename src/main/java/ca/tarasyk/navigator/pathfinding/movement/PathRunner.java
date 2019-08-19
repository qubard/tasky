package ca.tarasyk.navigator.pathfinding.movement;

import ca.tarasyk.navigator.BetterBlockPos;
import ca.tarasyk.navigator.NavigatorProvider;
import ca.tarasyk.navigator.pathfinding.path.BlockPosPath;
import ca.tarasyk.navigator.pathfinding.goal.Goal;
import ca.tarasyk.navigator.pathfinding.util.PlayerUtil;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class PathRunner implements Runnable {

    private final BlockPosPath path;

    public PathRunner(BlockPosPath path, Goal goal) {
        this.path = path;
    }

    private void runAlongPath() {
        List<BetterBlockPos> nodes = path.getNodes();
        EntityPlayer player = NavigatorProvider.getPlayer();

        int currIndex = path.nextClosest(0);
        long elapsedMoveTime = 0; // So we don't get stuck at a node (a little bit unpreventable)

        while (!Thread.currentThread().isInterrupted()) {
            if (currIndex + 1 < nodes.size()) {
                BetterBlockPos targetNode = path.getNode(currIndex + 1);

                PlayerUtil.lookAtXZ(targetNode);

                int nextIndex = path.nextClosest(currIndex);

                // Does the player have to jump up? (the target node is a block above), or in water
                PlayerUtil.moveForward();
                // We make sure the check is double because this messes up on crops, so we need to be exact
                PlayerUtil.jump(((double)targetNode.getY() - player.posY >= 1.0) || NavigatorProvider.getPlayer().isInWater());

                if ((nextIndex >= 0 && nextIndex > currIndex)){
                    currIndex = nextIndex;
                    elapsedMoveTime = System.currentTimeMillis();
                } else if (PlayerUtil.playerAt(targetNode) && (System.currentTimeMillis() - elapsedMoveTime) >= 300) {
                    currIndex++;
                }
            } else {
                BetterBlockPos node = path.getNode(nodes.size() - 1);
                double dx = player.posX - (node.getX() + 0.5);
                double dz = player.posZ - (node.getZ() + 0.5);
                float yaw = (float) (Math.atan2(dx, dz) * 180f / Math.PI);

                yaw = 180 - yaw;
                player.rotationYaw = yaw;

                if (PlayerUtil.playerAt(node)) {
                    PlayerUtil.stopMovingForward();
                    break;
                }
            }
        }
        NavigatorProvider.getMinecraft().gameSettings.keyBindForward.setKeyBindState(NavigatorProvider.getMinecraft().gameSettings.keyBindForward.getKeyCode(), false);
        NavigatorProvider.getMinecraft().gameSettings.keyBindJump.setKeyBindState(NavigatorProvider.getMinecraft().gameSettings.keyBindJump.getKeyCode(), false);
    }

    @Override
    public void run() {
        runAlongPath();
    }
}
