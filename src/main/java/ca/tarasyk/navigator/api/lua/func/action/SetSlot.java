package ca.tarasyk.navigator.api.lua.func.action;

import ca.tarasyk.navigator.NavigatorProvider;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

public class SetSlot extends OneArgFunction {
    @Override
    public LuaValue call(LuaValue arg) {
        int slot = arg.checkint();
        if (slot >= 36 && slot < 44) {
            NavigatorProvider.getPlayer().inventory.currentItem = slot - 36;
        }
        return null;
    }
}
