package ca.tarasyk.navigator.api.lua.func.action;

import ca.tarasyk.navigator.NavigatorProvider;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ZeroArgFunction;

public class AttackMouseover extends ZeroArgFunction {
    @Override
    public LuaValue call() {
        PlayerControllerMP controller = NavigatorProvider.getMinecraft().playerController;
        EntityPlayer player = NavigatorProvider.getPlayer();
        Entity ent = NavigatorProvider.getMinecraft().objectMouseOver.entityHit;
        if (ent != null) {
            controller.attackEntity(player, ent);
            player.swingArm(EnumHand.MAIN_HAND);
        }
        return null;
    }
}
