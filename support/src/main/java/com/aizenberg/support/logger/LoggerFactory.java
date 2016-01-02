package com.aizenberg.support.logger;

/**
 * Created by Yuriy Aizenberg
 */
public class LoggerFactory {

    public static Logger getLogger(String tag) {
        if (tag == null) throw new LoggerException("Tag can't be null");
        return new Logger(tag);
    }

    public static Logger getLogger(Class clazz) {
        if (clazz == null) throw new LoggerException("Class can't be null");
        return new Logger(clazz.getSimpleName());
    }
}
