package ca.tarasyk.navigator.api.lua;

import ca.tarasyk.navigator.api.lua.func.MoveTo;
import ca.tarasyk.navigator.api.lua.func.MoveToXZ;
import ca.tarasyk.navigator.api.lua.func.PrintChat;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

public class HookLib extends TwoArgFunction {
    @Override
    public LuaValue call(LuaValue modname, LuaValue env) {
        LuaValue root = tableOf();
        LuaValue table = tableOf();
        table.set("add", new HookAdd());
        env.set("hook", table);
        env.set("printChat", new PrintChat());
        env.set("moveTo", new MoveTo());
        env.set("moveToXZ", new MoveToXZ());
        return root;
    }

    /**
     * Add the hooked LuaFunction to the hook table
     */
    static class HookAdd extends TwoArgFunction {
        @Override
        public LuaValue call(LuaValue hook, LuaValue func) {
            HookProvider.getProvider().addHook(Hook.hookForName(hook.checkjstring()).get(), func.checkfunction());
            return null;
        }
    }
}
