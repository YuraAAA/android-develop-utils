package com.aizenberg.support.cache;

import com.aizenberg.support.cache.config.ChangeConfig;

import java.util.List;

/**
 * Created by Yuriy Aizenberg
 */
public interface ICache<C> {

    void put(String key, C data, Long expiredWhen);

    void put(String key, C data);

    void put(String key, List<C> data, Long expiredWhen);

    void put(String key, List<C> data);

    C get(String key);

    List<C> getCollection(String key);

    void setExpire(String key, Long expire);

    void evict(String key);

    void evictAll();

    void setConfig(ChangeConfig<C> config);

}
