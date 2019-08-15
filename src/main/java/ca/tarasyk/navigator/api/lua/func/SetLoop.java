package ca.tarasyk.navigator.api.lua.func;

import ca.tarasyk.navigator.NavigatorMod;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

public class SetLoop extends TwoArgFunction {
    @Override
    public LuaValue call(LuaValue arg1, LuaValue arg2) {
        LuaFunction func = arg1.checkfunction();
        long delayMs = arg2.checklong();
        NavigatorMod.executorService.submit(() -> {
            while (func.call().checkboolean()) {
                try {
                    Thread.sleep(delayMs);
                } catch (InterruptedException e) {}
            } // Call the function over and over so long as it returns true
        });
        return null;
    }
}
