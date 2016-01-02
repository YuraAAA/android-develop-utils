package com.aizenberg.support.event;

/**
 * Created by Yuriy Aizenberg
 */
public interface IEventReceiver {

    void onReceiveAction(String action, Object... args);

}
