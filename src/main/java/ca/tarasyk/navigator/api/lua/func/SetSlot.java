package ca.tarasyk.navigator.api.lua.func;

import ca.tarasyk.navigator.NavigatorProvider;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

public class SetSlot extends OneArgFunction {
    @Override
    public LuaValue call(LuaValue arg) {
        int slot = arg.checkint();
        if (slot >= 0 && slot < 9) {
            NavigatorProvider.getPlayer().inventory.currentItem = slot;
        }
        return null;
    }
}
