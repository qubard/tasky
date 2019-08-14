package ca.tarasyk.navigator.api.lua;

import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

public class HookProvider {
    private static HookProvider singleton = new HookProvider();
    private static Map<Hook, LuaFunction> hookTable;
    // Tbh, isn't ReentrantLock the best way to achieve a lock that isn't blocking?
    private static Map<Hook, ReentrantLock> lockTable;

    HookProvider() {
        hookTable = new HashMap<>();
        lockTable = new HashMap<>();
    }

    public void addHook(Hook hook, LuaFunction func) {
        hookTable.put(hook, func);
        lockTable.put(hook, new ReentrantLock());
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
                ReentrantLock lock = lockTable.get(hook);
                if (lock.tryLock()) {
                    try {
                        func.get().call(CoerceJavaToLua.coerce(event));
                    } finally {
                        lock.unlock();
                    }
                }
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
