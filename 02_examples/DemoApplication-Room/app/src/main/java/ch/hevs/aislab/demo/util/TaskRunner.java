package ch.hevs.aislab.demo.util;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TaskRunner {
    private static TaskRunner instance;
    private final Executor executor;
    private final Handler handler;

    private TaskRunner() {
        executor = Executors.newSingleThreadExecutor();
        handler = new Handler(Looper.getMainLooper());
    }

    public static TaskRunner getInstance(Class c) {
        if (instance == null) {
            synchronized (c) {
                if (instance == null) {
                    instance = new TaskRunner();
                }
            }
        }
        return instance;
    }

    public <R> void executeAsync(Callable<R> callable, OnAsyncEventListener callback) {
        executor.execute(() -> {
            Exception exception = null;
            try {
                callable.call();
            } catch (Exception e) {
                exception = e;
            }
            final Exception finalException = exception;
            handler.post(() -> {
                if (finalException == null) {
                    callback.onSuccess();
                } else {
                    callback.onFailure(finalException);
                }
            });
        });
    }
}