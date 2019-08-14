package ca.tarasyk.navigator.api.lua.func;

import ca.tarasyk.navigator.NavigatorProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ZeroArgFunction;

public class DropAll extends ZeroArgFunction {
    @Override
    public LuaValue call() {
        NavigatorProvider.getMinecraft().playerController.sendPacketDropItem(NavigatorProvider.getPlayer().inventory.getCurrentItem());
        return null;
    }
}
