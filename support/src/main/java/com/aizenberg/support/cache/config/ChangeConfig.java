package com.aizenberg.support.cache.config;

import com.aizenberg.support.common.NotificationType;
import com.aizenberg.support.event.IAction;

/**
 * Created by Yuriy Aizenberg
 */
public class ChangeConfig<T> {

    private ChangeConfig() {
    }

    private NotificationType notificationType;
    private IAction actionChange;
    private IAction actionChangeCollection;
    private String actionChangeString;
    private String actionChangeCollectionString;
    private ICacheChangeListener<T> listener;

    public static <T> ChangeConfig<T> createEventConfig(IAction actionChange, IAction actionChangeCollection) {
        ChangeConfig<T> changeConfig = new ChangeConfig<>();
        changeConfig.notificationType = NotificationType.EVENT;
        changeConfig.actionChange = actionChange;
        changeConfig.actionChangeCollection = actionChangeCollection;
        return changeConfig;
    }

    public static <T> ChangeConfig<T> createEventConfig(String actionChangeString, String actionChangeCollectionString) {
        ChangeConfig<T> changeConfig = new ChangeConfig<>();
        changeConfig.notificationType = NotificationType.EVENT;
        changeConfig.actionChangeString = actionChangeString;
        changeConfig.actionChangeCollectionString = actionChangeCollectionString;
        return changeConfig;

    }

    public static <T> ChangeConfig<T> createListenerConfig(ICacheChangeListener<T> listener) {
        ChangeConfig<T> changeConfig = new ChangeConfig<>();
        changeConfig.notificationType = NotificationType.LISTENER;
        changeConfig.listener = listener;
        return changeConfig;
    }


    public NotificationType getNotificationType() {
        return notificationType;
    }

    public IAction getActionChange() {
        return actionChange;
    }

    public String getActionChangeString() {
        return actionChangeString;
    }

    public ICacheChangeListener<T> getListener() {
        return listener;
    }

    public IAction getActionChangeCollection() {
        return actionChangeCollection;
    }

    public String getActionChangeCollectionString() {
        return actionChangeCollectionString;
    }
}
