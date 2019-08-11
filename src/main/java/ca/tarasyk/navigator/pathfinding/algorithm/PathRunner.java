package ca.tarasyk.navigator.pathfinding.algorithm;

import ca.tarasyk.navigator.BetterBlockPos;
import ca.tarasyk.navigator.NavigatorMod;
import ca.tarasyk.navigator.NavigatorProvider;
import ca.tarasyk.navigator.pathfinding.path.BlockPosPath;
import net.minecraft.entity.player.EntityPlayer;

import javax.swing.text.html.parser.Entity;
import java.util.List;

public class PathRunner implements Runnable {

    private final BlockPosPath path;

    public PathRunner(BlockPosPath path) {
        this.path = path;
    }

    @Override
    public void run() {
        // use path to find the closest point, then move to each point on the path
        NavigatorProvider.getMinecraft().gameSettings.autoJump = true;
        List<BetterBlockPos> nodes = path.getNodes();

        // or we could assume we're starting there
        int currIndex = 0;

        while (true) {
            if (currIndex + 1 < nodes.size()) {
                BetterBlockPos targetNode = path.getNode(currIndex + 1);
                EntityPlayer p = NavigatorProvider.getPlayer();
                double dx = p.posX - (double) (targetNode.getX() + 0.5);
                double dz = p.posZ - (double) (targetNode.getZ() + 0.5);
                float yaw = (float) (Math.atan2(dx, dz) * 180f / Math.PI);
                yaw = 180 - yaw;

                if (NavigatorProvider.getPlayer().isInWater()) {
                    NavigatorProvider.getMinecraft().gameSettings.keyBindForward.setKeyBindState(NavigatorProvider.getMinecraft().gameSettings.keyBindJump.getKeyCode(), true);
                }

                NavigatorProvider.getMinecraft().gameSettings.keyBindForward.setKeyBindState(NavigatorProvider.getMinecraft().gameSettings.keyBindForward.getKeyCode(), true);
                p.rotationYaw = yaw;

                NavigatorMod.printDebugMessage(currIndex + "->" + nodes.size() + "," + dx + "," + dz + "," + yaw);
                double d = Math.sqrt(Math.pow(targetNode.getX() - p.posX + 0.5, 2) + Math.pow(targetNode.getZ() - p.posZ + 0.5, 2));
                if (d <= Math.sqrt(2) / 2) {
                    currIndex++;
                    if (targetNode.equals(nodes.get(nodes.size() - 1))) {
                        break;
                    }
                }
            } else {
                NavigatorMod.printDebugMessage("index exceeds?.");
                break;
            }
        }
        NavigatorMod.printDebugMessage("Done.");
        NavigatorProvider.getMinecraft().gameSettings.keyBindForward.setKeyBindState(NavigatorProvider.getMinecraft().gameSettings.keyBindForward.getKeyCode(), false);
    }
}
