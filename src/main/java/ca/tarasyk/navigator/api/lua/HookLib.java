package ca.tarasyk.navigator.api.lua;

import ca.tarasyk.navigator.NavigatorMod;
import ca.tarasyk.navigator.NavigatorProvider;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

public class HookLib extends TwoArgFunction {
    @Override
    public LuaValue call(LuaValue modname, LuaValue env) {
        LuaValue root = tableOf();
        LuaValue table = tableOf();
        table.set("add", new HookAdd());
        env.set("hook", table);
        env.set("printChat", new PrintChat());
        return root;
    }

    static class PrintChat extends OneArgFunction {
        @Override
        public LuaValue call(LuaValue arg) {
            if (NavigatorProvider.getPlayer() != null) {
                NavigatorMod.printDebugMessage(arg.checkstring().tojstring());
            }
            return null;
        }
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
