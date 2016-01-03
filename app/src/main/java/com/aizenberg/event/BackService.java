package com.aizenberg.event;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

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
        UUID uuid = UUID.randomUUID();
        EventBus.getBus().notifyAction("computation", uuid);
        stopSelf();
    }
}
