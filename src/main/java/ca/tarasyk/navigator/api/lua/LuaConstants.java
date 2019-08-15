package ca.tarasyk.navigator.api.lua;

import org.luaj.vm2.LuaValue;

public class LuaConstants {
    public static final LuaValue TRUE = LuaValue.valueOf(true);
    public static final LuaValue FALSE = LuaValue.valueOf(false);
    public static final int THREAD_COUNT = 4;
}
