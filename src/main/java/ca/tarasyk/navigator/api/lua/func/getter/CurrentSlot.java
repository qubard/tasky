package ca.tarasyk.navigator.api.lua.func.getter;

import ca.tarasyk.navigator.pathfinding.util.PlayerUtil;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ZeroArgFunction;

public class CurrentSlot extends ZeroArgFunction {
    @Override
    public LuaValue call() {
        return LuaValue.valueOf(PlayerUtil.getCurrentSlot());
    }
}
