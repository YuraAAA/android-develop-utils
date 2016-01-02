package com.aizenberg.support.cache;

import com.aizenberg.support.cache.config.ChangeConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yuriy Aizenberg
 */
public class MemCache {

    private static Map<Class, ICache> cacheMap = new HashMap<>();

    private MemCache() {
    }

    private static <T> ICache<T> getCache(Class<T> clazz) {
        ICache<T> iCache = cacheMap.get(clazz);
        if (iCache == null) {
            iCache = new SynchronizedCache<>();
            cacheMap.put(clazz, iCache);
        }
        return iCache;
    }

    public static ICache<Object> defaultCache() {
        return getCache(Object.class);
    }

    public static <T> ICache<T> cache(Class<T> tClass) {
        return getCache(tClass);
    }


    public static <T> void addConfig(Class<T> tClass, ChangeConfig<T> config) {
        getCache(tClass).setConfig(config);
    }

}
