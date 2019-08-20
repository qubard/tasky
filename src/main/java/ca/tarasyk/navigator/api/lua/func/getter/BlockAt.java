package ca.tarasyk.navigator.api.lua.func.getter;

import ca.tarasyk.navigator.NavigatorProvider;
import net.minecraft.util.math.BlockPos;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

public class BlockAt extends ThreeArgFunction {
    @Override
    public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
        int x = (int) Math.floor(arg1.checkdouble());
        int y = (int) Math.floor(arg2.checkdouble());
        int z = (int) Math.floor(arg3.checkdouble());
        boolean isLoaded = NavigatorProvider.getWorld().getChunkFromChunkCoords(x >> 4, z >> 4).isLoaded();

        if (!isLoaded) {
            return null;
        }

        return CoerceJavaToLua.coerce(NavigatorProvider.getWorld().getBlockState(new BlockPos(x, y, z)).getBlock());
    }
}
