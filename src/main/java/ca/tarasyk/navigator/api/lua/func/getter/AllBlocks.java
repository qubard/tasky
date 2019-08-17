package ca.tarasyk.navigator.api.lua.func.getter;

import net.minecraft.init.Blocks;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

public class AllBlocks extends OneArgFunction {
    @Override
    public LuaValue call(LuaValue arg1) {
        String field = arg1.checkjstring();
        try {
            return CoerceJavaToLua.coerce(Blocks.class.getField(field));
        } catch (NoSuchFieldException e) {
            return null;
        }
    }
}
