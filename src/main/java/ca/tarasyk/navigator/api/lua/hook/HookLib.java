package ca.tarasyk.navigator.api.lua.hook;

import ca.tarasyk.navigator.api.lua.func.*;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

public class HookLib extends TwoArgFunction {
    @Override
    public LuaValue call(LuaValue modname, LuaValue env) {
        LuaValue root = tableOf();
        LuaValue table = tableOf();
        table.set("add", new HookAdd());
        env.set("hook", table);
        env.set("attack", new LeftClick());
        env.set("drop", new Drop());
        env.set("closeGUI", new CloseGUI());
        env.set("dropStack", new DropStack());
        env.set("putInChest", new PutInChest());
        env.set("printChat", new PrintChat());
        env.set("currentSlot", new CurrentSlot());
        env.set("moveTo", new MoveTo(30));
        env.set("moveToXZ", new MoveToXZ());
        env.set("rightClickBlock", new RightClickBlock());
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
