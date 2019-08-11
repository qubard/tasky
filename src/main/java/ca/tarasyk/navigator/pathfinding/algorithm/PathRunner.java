package ca.tarasyk.navigator.pathfinding.algorithm;

import ca.tarasyk.navigator.BetterBlockPos;
import ca.tarasyk.navigator.NavigatorMod;
import ca.tarasyk.navigator.NavigatorProvider;
import ca.tarasyk.navigator.pathfinding.path.BlockPosPath;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class PathRunner implements Runnable {

    private final BlockPosPath path;

    public PathRunner(BlockPosPath path) {
        this.path = path;
    }

    @Override
    public void run() {
        boolean prevAutoJump = NavigatorProvider.getMinecraft().gameSettings.autoJump;
        NavigatorProvider.getMinecraft().gameSettings.autoJump = true;
        List<BetterBlockPos> nodes = path.getNodes();

        EntityPlayer p = NavigatorProvider.getPlayer();

        int currIndex = path.indexForClosest(new BetterBlockPos(p.getPosition()), Heuristic.EUCLIDEAN_DISTANCE_3D);

        while (true) {
            if (currIndex + 1 < nodes.size()) {
                BetterBlockPos targetNode = path.getNode(currIndex + 1);
                double dx = p.posX - (targetNode.getX() + 0.5);
                double dz = p.posZ - (targetNode.getZ() + 0.5);
                float yaw = (float) (Math.atan2(dx, dz) * 180f / Math.PI);

                yaw = 180 - yaw;

                // Cheap "auto swim"
                if (NavigatorProvider.getPlayer().isInWater()) {
                    NavigatorProvider.getMinecraft().gameSettings.keyBindForward.setKeyBindState(NavigatorProvider.getMinecraft().gameSettings.keyBindJump.getKeyCode(), true);
                }

                NavigatorProvider.getMinecraft().gameSettings.keyBindForward.setKeyBindState(NavigatorProvider.getMinecraft().gameSettings.keyBindForward.getKeyCode(), true);
                p.rotationYaw = yaw;
                if ((int) p.posY == targetNode.getY()) {
                    p.rotationPitch = 5f;
                } else if (p.posY > targetNode.getY()){
                    p.rotationPitch = 40f;
                }

                NavigatorMod.printDebugMessage(currIndex + "->" + nodes.size() + "," + dx + "," + dz + "," + yaw);
                if (targetNode.equals(path.getNode(nodes.size() - 1)) && Heuristic.EUCLIDEAN_DISTANCE_3D.apply(new BetterBlockPos(p.getPosition()), new BetterBlockPos(targetNode.getX() + 0.5, p.posY, targetNode.getZ() + 0.5)) <= 0.3) {
                    break;
                }

                if (Math.sqrt(p.getDistanceSq(targetNode.getX() + 0.5, p.posY, targetNode.getZ() + 0.5)) < 0.2) {
                    currIndex++;
                }

                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                NavigatorMod.printDebugMessage("index exceeds?.");
                break;
            }
        }
        NavigatorMod.printDebugMessage("Done.");

        NavigatorProvider.getMinecraft().gameSettings.keyBindForward.setKeyBindState(NavigatorProvider.getMinecraft().gameSettings.keyBindForward.getKeyCode(), false);
        NavigatorProvider.getMinecraft().gameSettings.keyBindForward.setKeyBindState(NavigatorProvider.getMinecraft().gameSettings.keyBindJump.getKeyCode(), false);
        NavigatorProvider.getMinecraft().gameSettings.autoJump = prevAutoJump;
    }
}
