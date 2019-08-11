package ca.tarasyk.navigator.api.lua;

import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HookProvider {
    private static HookProvider singleton = new HookProvider();
    private static Map<Hook, LuaFunction> hookTable;

    HookProvider() {
        hookTable = new HashMap<>();
    }

    public void addHook(Hook hook, LuaFunction func) {
        hookTable.put(hook, func);
    }

    public void unhook() {
        hookTable.clear();
    }

    private Optional<LuaFunction> getFunction(Hook hook) {
        return Optional.ofNullable(hookTable.get(hook));
    }

    public boolean dispatch(Hook hook, Object event) {
        Optional<LuaFunction> func = getFunction(hook);
        if (func.isPresent()) {
            try {
                func.get().call(CoerceJavaToLua.coerce(event));
            } catch(LuaError e) {
                // In the event that a LuaError is thrown simply unhook
                this.unhook();
                return false;
            }
            return true;
        }
        return false;
    }

    public static HookProvider getProvider() {
        return singleton;
    }
}
