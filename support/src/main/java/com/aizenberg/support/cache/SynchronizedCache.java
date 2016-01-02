package com.aizenberg.support.cache;

import com.aizenberg.support.cache.config.ChangeConfig;
import com.aizenberg.support.event.EventBus;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Yuriy Aizenberg
 */
public class SynchronizedCache<C> implements ICache<C> {

    private Map<String, TimeCacheable<C>> classInstanceCache = new ConcurrentHashMap<>();
    private Map<String, TimeCacheable<List<C>>> listClassInstanceCache = new ConcurrentHashMap<>();
    private ChangeConfig<C> config;

    public static final Object MUTEX = new Object();

    SynchronizedCache() {
    }

    @Override
    public void put(String key, C data, Long expiredWhen) {
        synchronized (MUTEX) {
            classInstanceCache.put(key, new TimeCacheable<>(expiredWhen, data));
            notifyChange(data);
        }
    }

    @Override
    public void put(String key, C data) {
        synchronized (MUTEX) {
            classInstanceCache.put(key, new TimeCacheable<>(data));
            notifyChange(data);
        }
    }

    @Override
    public void put(String key, List<C> data, Long expiredWhen) {
        synchronized (MUTEX) {
            listClassInstanceCache.put(key, new TimeCacheable<>(expiredWhen, data));
            notifyChange(data);
        }
    }

    @Override
    public void put(String key, List<C> data) {
        synchronized (MUTEX) {
            listClassInstanceCache.put(key, new TimeCacheable<>(data));
            notifyChange(data);
        }
    }

    @Override
    public C get(String key) {
        synchronized (MUTEX) {
            TimeCacheable<C> cTimeCacheable = classInstanceCache.get(key);
            if (cTimeCacheable == null) return null;
            if (cTimeCacheable.isExpired()) {
                classInstanceCache.remove(key);
                return null;
            }
            return cTimeCacheable.getData();
        }
    }

    @Override
    public List<C> getCollection(String key) {
        synchronized (MUTEX) {
            TimeCacheable<List<C>> cTimeCacheable = listClassInstanceCache.get(key);
            if (cTimeCacheable == null) return null;
            if (cTimeCacheable.isExpired()) {
                classInstanceCache.remove(key);
                return null;
            }
            return cTimeCacheable.getData();
        }
    }

    @Override
    public void setExpire(String key, Long expire) {
        synchronized (MUTEX) {
            TimeCacheable<List<C>> listTimeCacheable = listClassInstanceCache.get(key);
            if (listTimeCacheable != null) {
                TimeCacheable<List<C>> newCache = new TimeCacheable<>(expire, listTimeCacheable.getData());
                listClassInstanceCache.put(key, newCache);
                notifyChange(newCache.getData());
            } else {
                TimeCacheable<C> cTimeCacheable = classInstanceCache.get(key);
                if (cTimeCacheable != null) {
                    TimeCacheable<C> newCache = new TimeCacheable<>(expire, cTimeCacheable.getData());
                    classInstanceCache.put(key, newCache);
                    notifyChange(newCache.getData());
                }
            }
        }
    }

    @Override
    public void evict(String key) {
        synchronized (MUTEX) {
            listClassInstanceCache.remove(key);
            classInstanceCache.remove(key);
            notifyChange((C) null);
            notifyChange((List<C>) null);
        }
    }

    @Override
    public void evictAll() {
        synchronized (MUTEX) {
            listClassInstanceCache.clear();
            classInstanceCache.clear();
            notifyChange((C) null);
            notifyChange((List<C>) null);
        }
    }

    private void notifyChange(C data) {
        if (config != null && config.getNotificationType() != null) {
            switch (config.getNotificationType()) {
                case LISTENER:
                    if (config.getListener() != null) {
                        config.getListener().onChange(data);
                    }
                    break;
                case EVENT:
                    if (config.getActionChange() != null) {
                        EventBus.getBus().notifyAction(config.getActionChange(), data);
                    } else if (config.getActionChangeString() != null) {
                        EventBus.getBus().notifyAction(config.getActionChangeString(), data);
                    }
                    break;
            }
        }
    }


    private void notifyChange(List<C> data) {
        if (config != null && config.getNotificationType() != null) {
            switch (config.getNotificationType()) {
                case LISTENER:
                    if (config.getListener() != null) {
                        config.getListener().onChange(data);
                    }
                    break;
                case EVENT:
                    if (config.getActionChangeCollection() != null) {
                        EventBus.getBus().notifyAction(config.getActionChangeCollection(), data);
                    } else if (config.getActionChangeCollectionString() != null) {
                        EventBus.getBus().notifyAction(config.getActionChangeCollectionString(), data);
                    }
                    break;
            }
        }
    }


    @Override
    public void setConfig(ChangeConfig<C> config) {
        this.config = config;
    }


}
