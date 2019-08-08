package ca.tarasyk.navigator;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;

public class NavigatorProvider {
    public static Minecraft getMinecraft() {
        return Minecraft.getMinecraft();
    }

    public static WorldClient getWorld() {
        return getMinecraft().world;
    }

    public static EntityPlayer getPlayer() {
        return getMinecraft().player;
    }
}
