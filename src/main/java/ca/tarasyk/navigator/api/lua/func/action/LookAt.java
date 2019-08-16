package ca.tarasyk.navigator.api.lua.func.action;

import ca.tarasyk.navigator.pathfinding.util.PlayerUtil;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ThreeArgFunction;

public class LookAt extends ThreeArgFunction {
    @Override
    public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
        int x = (int) Math.floor(arg1.checkdouble());
        int y = (int) Math.floor(arg2.checkdouble());
        int z = (int) Math.floor(arg3.checkdouble());
        PlayerUtil.lookAtXYZ(x, y, z);
        return null;
    }
}
