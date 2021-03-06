package ca.tarasyk.navigator.pathfinding.util;

import ca.tarasyk.navigator.BetterBlockPos;
import ca.tarasyk.navigator.NavigatorProvider;
import ca.tarasyk.navigator.pathfinding.movement.Move;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

public class PlayerUtil {
    public static void lookAtXZ(BetterBlockPos pos) {
        EntityPlayer p = NavigatorProvider.getPlayer();
        double dx = p.posX - (pos.getX() + 0.5);
        double dz = p.posZ - (pos.getZ() + 0.5);
        float yaw = 180 - (float) (Math.atan2(dx, dz) * 180f / Math.PI);
        p.rotationYaw = yaw;
        p.rotationPitch = 0f;
    }

    public static float yawFrom(BetterBlockPos src, BetterBlockPos dest) {
        double dx = src.getX() - dest.getX();
        double dz = src.getZ() - dest.getZ();
        float yaw = 180 - (float) (Math.atan2(dx, dz) * 180f / Math.PI);
        return yaw;
    }

    public static float yawFrom(double x, double z, BetterBlockPos dest, int dx1, int dz1) {
        double dx = x - dest.getX() - 0.5 - 0.5 * Integer.compare(dx1, 0);
        double dz = z - dest.getZ() - 0.5 - 0.5 * Integer.compare(dz1, 0);
        float yaw = 180 - (float) (Math.atan2(dx, dz) * 180f / Math.PI);
        return yaw;
    }

    /**
     * @param pos The position that might be in water
     * @return If it's in water, return the surface position
     */
    public static BetterBlockPos inWaterReplacement(BetterBlockPos pos) {
        if (Move.isWater(pos.up()) && Move.isWater(pos) && Move.isWater(pos.down())) {
            return new BetterBlockPos(NavigatorProvider.getWorld().getTopSolidOrLiquidBlock(pos));
        }
        return pos;
    }

    public static void lookAtXZ(BetterBlockPos pos, float maxYawDelta) {
        EntityPlayer p = NavigatorProvider.getPlayer();
        double dx = p.posX - (pos.getX() + 0.5);
        double dz = p.posZ - (pos.getZ() + 0.5);
        float yaw = 180 - (float) (Math.atan2(dx, dz) * 180f / Math.PI);
        float delta = Math.abs(yaw - p.rotationYaw);
        if (delta <= maxYawDelta) {
            p.rotationYaw = yaw;
        }
        p.rotationPitch = 0f;
    }

    public static void lookAtXZ(int x, int z) {
        lookAtXZ(new BetterBlockPos(x, z));
    }

    public static boolean entityAt(Entity e, BetterBlockPos pos) {
        return Math.floor(e.posX) == pos.getX() && Math.floor(e.posZ) == pos.getZ();
    }

    public static boolean entityAtXYZ(Entity e, BetterBlockPos pos) {
        return Math.floor(e.posX) == pos.getX() && Math.floor(e.posZ) == pos.getZ() && Math.floor(e.posY) == pos.getY();
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

    public static boolean canReach(int x, int y, int z) {
        EntityPlayer player = NavigatorProvider.getPlayer();
        float reach = NavigatorProvider.getMinecraft().playerController.getBlockReachDistance();
        return Math.sqrt(player.getDistanceSqToCenter(new BlockPos(x, y, z))) <= reach;
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

    public static void attack(boolean state) {
        KeyBinding.setKeyBindState(NavigatorProvider.getMinecraft().gameSettings.keyBindAttack.getKeyCode(), state);
    }

    public static int getCurrentSlot() {
        return NavigatorProvider.getPlayer().inventory.currentItem;
    }

    public static void moveStack(Container container, int src, int dest, int waitDelayMs) {
        NavigatorProvider.getMinecraft().playerController.windowClick(
                container.windowId,
                src,
                0,
                ClickType.PICKUP,
                NavigatorProvider.getPlayer());

        // We need to wait before dropping, otherwise it happened too fast
        try {
            Thread.sleep(waitDelayMs);
        } catch (InterruptedException e) {}

        NavigatorProvider.getMinecraft().playerController.windowClick(
                container.windowId,
                dest,
                0,
                ClickType.PICKUP,
                NavigatorProvider.getPlayer());

        try {
            Thread.sleep(waitDelayMs);
        } catch (InterruptedException e) {}
        // Put the other item back

        NavigatorProvider.getMinecraft().playerController.windowClick(
                container.windowId,
                src,
                0,
                ClickType.PICKUP,
                NavigatorProvider.getPlayer());
    }
}
