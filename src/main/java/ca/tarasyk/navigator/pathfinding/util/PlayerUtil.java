package ca.tarasyk.navigator.pathfinding.util;

import ca.tarasyk.navigator.BetterBlockPos;
import ca.tarasyk.navigator.NavigatorProvider;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerUtil {
    public static void lookAt(BetterBlockPos pos) {
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

    public static void jump(boolean state) {
        NavigatorProvider.getMinecraft().gameSettings.keyBindJump.setKeyBindState(NavigatorProvider.getMinecraft().gameSettings.keyBindJump.getKeyCode(), state);
    }
}
