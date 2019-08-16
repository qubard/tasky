package ca.tarasyk.navigator.api.lua.func.action;

import ca.tarasyk.navigator.NavigatorMod;
import ca.tarasyk.navigator.NavigatorProvider;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

public class PrintChat extends OneArgFunction {
    @Override
    public LuaValue call(LuaValue arg) {
        if (NavigatorProvider.getPlayer() != null) {
            NavigatorMod.printDebugMessage(arg.checkstring().tojstring());
        }
        return null;
    }
}
