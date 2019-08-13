package ca.tarasyk.navigator.pathfinding.util;

import ca.tarasyk.navigator.BetterBlockPos;
import ca.tarasyk.navigator.NavigatorProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerUtil {
    public static void lookAtXZ(BetterBlockPos pos) {
        EntityPlayer p = NavigatorProvider.getPlayer();
        double dx = p.posX - (double) (pos.getX() + 0.5);
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

    public static boolean entityAt(Entity e, BetterBlockPos pos) {
        double a = e.posX < 0 ? Math.ceil(e.posX) : Math.floor(e.posX);
        double b = e.posZ < 0 ? Math.ceil(e.posZ) : Math.floor(e.posZ);
        double c = pos.getX() + 0.5 < 0 ? Math.ceil(pos.getX() + 0.5) : Math.floor(pos.getX()+ 0.5 );
        double d = pos.getZ() + 0.5 < 0 ? Math.ceil(pos.getZ() + 0.5) : Math.floor(pos.getZ()+ 0.5 );

        return a == c && b == d;
    }

    public static boolean playerAt(BetterBlockPos pos) {
        return entityAt(NavigatorProvider.getPlayer(), pos);
    }

    public static void jump(boolean state) {
        NavigatorProvider.getMinecraft().gameSettings.keyBindJump.setKeyBindState(NavigatorProvider.getMinecraft().gameSettings.keyBindJump.getKeyCode(), state);
    }

    public static void moveForward() {
        NavigatorProvider.getMinecraft().gameSettings.keyBindForward.setKeyBindState(NavigatorProvider.getMinecraft().gameSettings.keyBindForward.getKeyCode(), true);
    }

    public static void stopMovingForward() {
        NavigatorProvider.getPlayer().moveForward = 0.0f;
        NavigatorProvider.getMinecraft().gameSettings.keyBindForward.setKeyBindState(NavigatorProvider.getMinecraft().gameSettings.keyBindForward.getKeyCode(), false);
    }
}
