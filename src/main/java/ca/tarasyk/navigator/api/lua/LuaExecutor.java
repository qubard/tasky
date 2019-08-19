package ca.tarasyk.navigator.api.lua;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class LuaExecutor {

    private int nThreads;
    private Optional<ExecutorService> executor;
    private static LuaExecutor singleton = new LuaExecutor(LuaConstants.THREAD_COUNT);
    private Optional<Future> loopFuture = Optional.ofNullable(null);

    public LuaExecutor(int nThreads) {
        this.nThreads = nThreads;
        this.executor = Optional.of(Executors.newFixedThreadPool(nThreads));
    }

    public void setLoopFuture(Future loopFuture) {
        this.loopFuture = Optional.ofNullable(loopFuture);
    }

    public void cancelActiveLoop() {
        if (loopFuture.isPresent()) {
            loopFuture.get().cancel(true);
            setLoopFuture(null);
        }
    }

    public void newExecutor() {
        executor = Optional.of(Executors.newFixedThreadPool(nThreads));
    }

    public static LuaExecutor get() {
        return singleton;
    }

    public Optional<ExecutorService> getExecutor() {
        synchronized (executor) {
            return executor;
        }
    }

    public Future submit(Callable callable) {
        synchronized (executor) {
            return executor.get().submit(callable);
        }
    }

    public Future submit(Runnable run) {
        synchronized (executor) {
            return executor.get().submit(run);
        }
    }
}
