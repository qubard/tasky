package ca.tarasyk.navigator.api.lua.func;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

public class Sleep extends OneArgFunction {
    @Override
    public LuaValue call(LuaValue arg) {
        long timeMs = Math.max(arg.checklong(), 0);
        try {
            Thread.sleep(timeMs);
        } catch (InterruptedException e) {
            // Do nothing
        }
        return null;
    }
}
