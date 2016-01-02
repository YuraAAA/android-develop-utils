package com.aizenberg.support.event;

import com.aizenberg.support.common.collection.NullExcludeSafetyList;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by Yuriy Aizenberg
 */
public class SynchronizedSafetyEventMultiMap<V extends IEventReceiver> {

    private final Map<String, List<V>> map = new ConcurrentHashMap<>();

    public synchronized void addListener(String key, V object) {
        List<V> vs = map.get(key);
        if (vs == null) {
            vs = new NullExcludeSafetyList<>();
            map.put(key, vs);
        }
        if (!vs.contains(object)) {
            vs.add(object);
        }
    }

    public synchronized void removeListeners(String key) {
        List<V> vs = map.get(key);
        if (vs != null) vs.clear();
    }

    public synchronized void removeListener(String key, V listener) {
        List<V> vs = map.get(key);
        if (vs == null || vs.isEmpty()) return;
        if (vs.contains(listener)) {
            vs.remove(listener);
        }
    }

    public synchronized boolean hasListenersByKey(String key) {
        List<V> vs = map.get(key);
        return vs != null && !vs.isEmpty();
    }

    public synchronized boolean notifyAboutEvent(String key, Object... args) {
        if (!hasListenersByKey(key)) return false;
        for (V v : map.get(key)) {
            v.onReceiveAction(key, args);
        }
        return true;
    }

    public synchronized boolean notifyAboutEvent(String key) {
        return notifyAboutEvent(key, new Object[0]);
    }

    public synchronized boolean notifyById(String key, Long id) {
        boolean result = false;
        if (hasListenersByKey(key)) {
            for (V v : map.get(key)) {
                if (v instanceof IEventIdentificationReceiver) {
                    if (IEventIdentificationReceiver.class.cast(v).getIdentifier().equals(id) || ((IEventIdentificationReceiver) v).getIdentifier() == IEventIdentificationReceiver.ANY) {
                        v.onReceiveAction(key);
                        result = true;
                    }
                }
            }
            return result;
        } else {
            return false;
        }
    }

}
