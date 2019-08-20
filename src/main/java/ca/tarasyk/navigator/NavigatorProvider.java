package ca.tarasyk.navigator;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;

public class NavigatorProvider {
    public static Minecraft getMinecraft() {
        return Minecraft.getMinecraft();
    }

    public static WorldClient getWorld() {
        return getMinecraft().world;
    }

    public static EntityPlayerSP getPlayer() {
        return getMinecraft().player;
    }
}
