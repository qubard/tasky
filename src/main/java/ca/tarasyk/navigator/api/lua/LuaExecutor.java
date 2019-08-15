package ca.tarasyk.navigator.api.lua;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LuaExecutor {

    private ExecutorService executor;
    private static LuaExecutor singleton = new LuaExecutor(LuaConstants.THREAD_COUNT);

    public LuaExecutor(int nThreads) {
        this.executor = Executors.newFixedThreadPool(nThreads);
    }

    public static LuaExecutor get() {
        return singleton;
    }

    public ExecutorService getExecutor() {
        synchronized (executor) {
            return executor;
        }
    }
}
