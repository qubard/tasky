package ca.tarasyk.navigator.api.lua.func.action;

import ca.tarasyk.navigator.pathfinding.util.PlayerUtil;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

public class Drop extends OneArgFunction {
    @Override
    public LuaValue call(LuaValue arg1) {
        boolean all = arg1.checkboolean();
        PlayerUtil.dropHeld(all);
        return null;
    }
}
