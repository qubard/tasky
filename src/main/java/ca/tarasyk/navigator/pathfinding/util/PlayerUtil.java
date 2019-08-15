package ca.tarasyk.navigator.pathfinding.util;

import ca.tarasyk.navigator.BetterBlockPos;
import ca.tarasyk.navigator.NavigatorMod;
import ca.tarasyk.navigator.NavigatorProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;

public class PlayerUtil {
    public static void lookAtXZ(BetterBlockPos pos) {
        EntityPlayer p = NavigatorProvider.getPlayer();
        double dx = p.posX - (pos.getX() + 0.5);
        double dz = p.posZ - (pos.getZ() + 0.5);
        float yaw = 180 - (float) (Math.atan2(dx, dz) * 180f / Math.PI);
        p.rotationYaw = yaw;
        p.rotationPitch = 0f;
    }

    public static void lookAtXZ(int x, int z) {
        lookAtXZ(new BetterBlockPos(x, z));
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

    /**
     * @param all Whether or not we drop the entire stack
     */
    public static void dropHeld(boolean all) {
        NavigatorProvider.getMinecraft().playerController.windowClick(
                0,
                36 + NavigatorProvider.getPlayer().inventory.currentItem,
                all ? 1 : 0,
                ClickType.THROW,
                NavigatorProvider.getPlayer());
    }

    public static void lookAtXYZ(BetterBlockPos pos) {
        EntityPlayer p = NavigatorProvider.getPlayer();
        double dx = p.posX - (pos.getX() + 0.5);
        double dz = p.posZ - (pos.getZ() + 0.5);
        double dy = (p.posY + p.getEyeHeight() - (pos.getY() + 0.5));
        double mag = Math.sqrt(dx * dx + dy * dy + dz * dz);
        float yaw = 180 - (float) (Math.atan2(dx, dz) * 180f / Math.PI);
        float pitch = (float) (Math.asin(dy / mag) * 180f / Math.PI);
        p.rotationYaw = yaw;
        p.rotationPitch = pitch;
    }

    public static void lookAtXYZ(int x, int y, int z) {
        lookAtXYZ(new BetterBlockPos(x, y, z));
    }
}
