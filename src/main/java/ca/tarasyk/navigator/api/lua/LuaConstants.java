package ca.tarasyk.navigator.api.lua;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

public class LuaConstants {
    public static final LuaValue TRUE = CoerceJavaToLua.coerce(true);
    public static final LuaValue FALSE = CoerceJavaToLua.coerce(false);
}
