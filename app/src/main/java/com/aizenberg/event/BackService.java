package com.aizenberg.event;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.aizenberg.support.cache.MemCache;
import com.aizenberg.support.event.EventBus;

import java.util.UUID;

/**
 * Created by Yuriy Aizenberg
 */
public class BackService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MemCache.cache(String.class).put("Key", "Value");

        MemCache.cache(String.class).evict("Key");

        MemCache.cache(String.class).evictAll();




        UUID uuid = UUID.randomUUID();
        EventBus.getBus().notifyAction(Events.COMPUTATION, uuid);
        boolean sendResult = EventBus.getBus().notifyById("computation", 1L, uuid);
        stopSelf();
    }
}
