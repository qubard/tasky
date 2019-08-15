package ca.tarasyk.navigator.api.lua.func.getters;

import ca.tarasyk.navigator.NavigatorProvider;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ZeroArgFunction;

public class ExperienceLevel extends ZeroArgFunction {
    @Override
    public LuaValue call() {
        return LuaValue.valueOf(NavigatorProvider.getPlayer().experienceLevel);
    }
}
