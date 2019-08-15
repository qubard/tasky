package ca.tarasyk.navigator.api.lua.func;

import ca.tarasyk.navigator.pathfinding.util.PlayerUtil;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ZeroArgFunction;

public class Drop extends ZeroArgFunction {
    @Override
    public LuaValue call() {
        PlayerUtil.dropHeld(false);
        return null;
    }
}