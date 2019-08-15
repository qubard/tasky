package ca.tarasyk.navigator.api.lua;

import ca.tarasyk.navigator.NavigatorMod;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class LuaExecutor {

    private final ExecutorService executor;
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

    public Future submit(Callable callable) {
        synchronized (executor) {
            return executor.submit(callable);
        }
    }

    public Future submit(Runnable run) {
        synchronized (executor) {
            return executor.submit(run);
        }
    }
}
