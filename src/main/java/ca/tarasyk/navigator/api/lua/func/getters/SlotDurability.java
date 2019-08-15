package ca.tarasyk.navigator.api.lua.func.getters;

import ca.tarasyk.navigator.NavigatorProvider;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

public class SlotDurability extends OneArgFunction {
    @Override
    public LuaValue call(LuaValue arg) {
        int slot = arg.checkint();
        return LuaValue.valueOf(NavigatorProvider.getPlayer().inventory.getStackInSlot(36 + slot).getItemDamage());
    }
}
