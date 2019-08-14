package ca.tarasyk.navigator.pathfinding.util;

import ca.tarasyk.navigator.BetterBlockPos;
import ca.tarasyk.navigator.NavigatorProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerUtil {
    public static void lookAtXZ(BetterBlockPos pos) {
        EntityPlayer p = NavigatorProvider.getPlayer();
        double dx = p.posX - (pos.getX() + 0.5);
        double dz = p.posZ - (pos.getZ() + 0.5);
        float yaw = 180 - (float) (Math.atan2(dx, dz) * 180f / Math.PI);
        p.rotationYaw = yaw;
        p.rotationPitch = 0f;
    }

    public static boolean entityAt(Entity e, BetterBlockPos pos) {
        return Math.floor(e.posX) == pos.getX() && Math.floor(e.posZ) == pos.getZ();
    }

    public static boolean playerAt(BetterBlockPos pos) {
        return entityAt(NavigatorProvider.getPlayer(), pos);
    }

    public static void jump(boolean state) {
        NavigatorProvider.getMinecraft().gameSettings.keyBindJump.setKeyBindState(NavigatorProvider.getMinecraft().gameSettings.keyBindJump.getKeyCode(), state);
    }

    public static void moveForward() {
        NavigatorProvider.getPlayer().moveForward = 1.0f;
        NavigatorProvider.getMinecraft().gameSettings.keyBindForward.setKeyBindState(NavigatorProvider.getMinecraft().gameSettings.keyBindForward.getKeyCode(), true);
    }

    public static void stopMovingForward() {
        NavigatorProvider.getPlayer().moveForward = 0.0f;
        NavigatorProvider.getMinecraft().gameSettings.keyBindForward.setKeyBindState(NavigatorProvider.getMinecraft().gameSettings.keyBindForward.getKeyCode(), false);
    }
}
