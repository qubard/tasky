package ca.tarasyk.navigator.api.lua.func.action;

import ca.tarasyk.navigator.pathfinding.util.PlayerUtil;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ZeroArgFunction;

public class DropStack extends ZeroArgFunction {
    @Override
    public LuaValue call() {
        PlayerUtil.dropHeld(true);
        return null;
    }
}
