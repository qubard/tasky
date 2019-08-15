package ca.tarasyk.navigator.api.lua.hook;

import ca.tarasyk.navigator.api.lua.func.*;
import ca.tarasyk.navigator.api.lua.func.getters.CurrentSlot;
import ca.tarasyk.navigator.api.lua.func.getters.ExperienceLevel;
import ca.tarasyk.navigator.api.lua.func.getters.FoodStats;
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
        env.set("sleep", new Sleep());
        env.set("lookAt", new LookAt());
        env.set("leftClick", new LeftClick());
        env.set("closeGUI", new CloseGUI());
        env.set("expLevel", new ExperienceLevel());
        env.set("dropStack", new DropStack());
        env.set("foodStats", new FoodStats());
        env.set("digBlock", new DigBlock());
        env.set("countMobsAt", new CountMobsAt());
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
