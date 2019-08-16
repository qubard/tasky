package ca.tarasyk.navigator.api.lua.func.action;

import ca.tarasyk.navigator.NavigatorProvider;
import ca.tarasyk.navigator.api.lua.LuaConstants;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

import java.util.Optional;

public class PutInContainer extends OneArgFunction {
    @Override
    public LuaValue call(LuaValue arg1) {
        int slot = arg1.checkint();

        if (slot < 0) {
            return LuaConstants.FALSE;
        }

        // Shift click into the container
        Optional<Container> container = Optional.ofNullable(NavigatorProvider.getPlayer().openContainer);

        if (!container.isPresent()) {
            return LuaConstants.FALSE;
        }

        NavigatorProvider.getMinecraft().playerController.windowClick(
                container.get().windowId,
                slot,
                0,
                ClickType.QUICK_MOVE,
                NavigatorProvider.getPlayer());

        return LuaConstants.TRUE;
    }
}
