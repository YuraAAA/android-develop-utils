package com.aizenberg.support.logger;

import android.util.Log;

/**
 * Created by Yuriy Aizenberg
 */
public class Logger {

    private String tag;

    Logger(String tag) {
        this.tag = tag;
    }

    public void v(String msg) {
        if (canPrint(LogLevel.V)) {
            Log.v(tag, msg);
        }
    }


    public void v(String msg, Throwable tr) {
        if (canPrint(LogLevel.V)) {
            Log.v(tag, msg, tr);
        }
    }

    public void d(String msg) {
        if (canPrint(LogLevel.D)) {
            Log.d(tag, msg);
        }
    }


    public void d(String msg, Throwable tr) {
        if (canPrint(LogLevel.D)) {
            Log.d(tag, msg, tr);
        }
    }

    public void i(String msg) {
        if (canPrint(LogLevel.I)) {
            Log.i(tag, msg);
        }

    }


    public void i(String msg, Throwable tr) {
        if (canPrint(LogLevel.I)) {
            Log.i(tag, msg, tr);
        }

    }


    public void w(String msg) {
        if (canPrint(LogLevel.W)) {
            Log.w(tag, msg);
        }

    }


    public void w(String msg, Throwable tr) {
        if (canPrint(LogLevel.W)) {
            Log.w(tag, msg, tr);
        }

    }


    public void w(Throwable tr) {
        if (canPrint(LogLevel.W)) {
            Log.w(tag, tr);
        }

    }


    public void e(String msg) {
        if (canPrint(LogLevel.E)) {
            Log.e(tag, msg);
        }

    }


    public void e(String msg, Throwable tr) {
        if (canPrint(LogLevel.E)) {
            Log.e(tag, msg, tr);
        }
    }


    private boolean canPrint(LogLevel level) {
        LoggerEnvironment.Environment environment = LoggerEnvironment.getEnvironment();
        if (environment == LoggerEnvironment.Environment.PROD) return false;
        if (environment == LoggerEnvironment.Environment.DEV) return true;
        if (environment == LoggerEnvironment.Environment.CUSTOM
                && (LoggerEnvironment.getLevelsList().contains(LogLevel.ALL)
                || LoggerEnvironment.getLevelsList().contains(level))) return true;
        return false;

    }
}
