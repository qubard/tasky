package ca.tarasyk.navigator.api.lua.func.getters;

import ca.tarasyk.navigator.NavigatorProvider;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ZeroArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

public class FoodStats extends ZeroArgFunction {
    @Override
    public LuaValue call() {
        return CoerceJavaToLua.coerce(NavigatorProvider.getPlayer().getFoodStats());
    }
}