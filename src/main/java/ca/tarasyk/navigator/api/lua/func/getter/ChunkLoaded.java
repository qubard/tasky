package ca.tarasyk.navigator.api.lua.func.getter;

import ca.tarasyk.navigator.NavigatorProvider;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

public class ChunkLoaded extends TwoArgFunction {
    @Override
    public LuaValue call(LuaValue arg1, LuaValue arg2) {
        int x = (int) Math.floor(arg1.checkdouble());
        int z = (int) Math.floor(arg2.checkdouble());
        return LuaValue.valueOf(NavigatorProvider.getWorld().getChunkFromChunkCoords(x >> 4, z >> 4).isLoaded());
    }
}
