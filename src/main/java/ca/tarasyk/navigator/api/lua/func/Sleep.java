package ca.tarasyk.navigator.api.lua.func;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

public class Sleep extends OneArgFunction {
    @Override
    public LuaValue call(LuaValue arg) {
        long time = arg.checklong();
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            // Do nothing
        }
        return null;
    }
}
