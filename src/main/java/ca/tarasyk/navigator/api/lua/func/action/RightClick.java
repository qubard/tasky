package ca.tarasyk.navigator.api.lua.func.action;

import ca.tarasyk.navigator.NavigatorProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ZeroArgFunction;

public class RightClick extends ZeroArgFunction {
    @Override
    public LuaValue call() {
        EntityPlayer player = NavigatorProvider.getPlayer();
        EnumActionResult result = NavigatorProvider.getMinecraft().playerController.processRightClick(player, NavigatorProvider.getWorld(), EnumHand.MAIN_HAND);
        return LuaValue.valueOf(result == EnumActionResult.SUCCESS);
    }
}
