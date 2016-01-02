package com.aizenberg.support.logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Yuriy Aizenberg
 */
public class LoggerEnvironment {

    private static Environment environment = Environment.DEV;

    private static final List<LogLevel> LEVELS_LIST = new ArrayList<>();

    public static void setDev() {
        LoggerEnvironment.environment = Environment.DEV;
    }

    public static void setProd() {
        LoggerEnvironment.environment = Environment.PROD;
    }

    public static void setCustom(LogLevel... levels) {
        if (levels == null) levels = new LogLevel[]{LogLevel.ALL};
        LEVELS_LIST.addAll(Arrays.asList(levels));
    }

    static List<LogLevel> getLevelsList() {
        return LEVELS_LIST;
    }

    public static Environment getEnvironment() {
        return environment;
    }

    public enum Environment {
        DEV,
        PROD,
        CUSTOM
    }



}
