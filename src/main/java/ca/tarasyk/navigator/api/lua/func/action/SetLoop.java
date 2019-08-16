package ca.tarasyk.navigator.api.lua.func.action;

import ca.tarasyk.navigator.api.lua.LuaExecutor;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

public class SetLoop extends TwoArgFunction {
    @Override
    public LuaValue call(LuaValue arg1, LuaValue arg2) {
        LuaFunction func = arg1.checkfunction();
        long delayMs = Math.max(0, arg2.checklong());
        LuaExecutor.get().cancelActiveLoop();
        LuaExecutor.get().setLoopFuture(LuaExecutor.get().submit(() -> {
            while (LuaExecutor.get().isLoopActive() && func.call().checkboolean()) {
                try {
                    Thread.sleep(delayMs);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }));
        return null;
    }
}
