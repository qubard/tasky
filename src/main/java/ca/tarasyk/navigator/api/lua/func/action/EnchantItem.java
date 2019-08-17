package ca.tarasyk.navigator.api.lua.func.action;

import ca.tarasyk.navigator.NavigatorProvider;
import ca.tarasyk.navigator.api.lua.LuaConstants;
import net.minecraft.inventory.Container;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

import java.util.Optional;

public class EnchantItem extends OneArgFunction {
    @Override
    public LuaValue call(LuaValue arg) {
        int button = arg.checkint() % 3;
        Optional<Container> container = Optional.ofNullable(NavigatorProvider.getPlayer().openContainer);

        if (!container.isPresent()) {
            return LuaConstants.FALSE;
        }

        NavigatorProvider.getMinecraft().playerController.sendEnchantPacket(container.get().windowId, button);
        return LuaConstants.TRUE;
    }
}
