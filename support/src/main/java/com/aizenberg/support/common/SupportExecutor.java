package com.aizenberg.support.common;

import android.os.AsyncTask;

import com.aizenberg.support.common.error.SupportException;

import java.util.concurrent.Executor;

/**
 * Created by Yuriy Aizenberg
 */
public class SupportExecutor {

    private static Executor executor;
    private static ExecutorType executorType = ExecutorType.SINGLE_THREAD;

    public enum ExecutorType {
        SINGLE_THREAD,
        CUSTOM
    }

    public static void setDefaultSingleThreadExecutor() {
        executorType = ExecutorType.SINGLE_THREAD;
    }

    public static void setCustomThreadExecutor(Executor executor) {
        if (executor == null) throw new SupportException("Executor can't be null");
        executorType = ExecutorType.CUSTOM;
        SupportExecutor.executor = executor;
    }

    public static Executor getExecutor() {
        return executor;
    }

    public static ExecutorType getExecutorType() {
        return executorType;
    }

    public static boolean isSimple() {
        return executorType == ExecutorType.SINGLE_THREAD;
    }

    @SafeVarargs
    public static <P, U, R> void execute(AsyncTask<P, U, R> task, P... args) {
        if (task == null) return;
        switch (executorType) {
            case SINGLE_THREAD:
                task.execute(args);
                break;
            case CUSTOM:
                task.executeOnExecutor(executor, args);
                break;
        }
    }

}
