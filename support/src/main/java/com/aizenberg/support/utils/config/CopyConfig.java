package com.aizenberg.support.utils.config;

import com.aizenberg.support.common.NotificationType;
import com.aizenberg.support.event.IAction;
import com.aizenberg.support.utils.ICopyListener;

/**
 * Created by Yuriy Aizenberg
 */
public class CopyConfig {

    private ICopyListener copyListener;
    private IAction actionOk;
    private IAction actionFail;

    private String actionOkString;
    private String actionFailString;

    private boolean asAction;


    private NotificationType notificationType;
    private Long id;

    CopyConfig(ICopyListener copyListener) {
        this.copyListener = copyListener;
        notificationType = NotificationType.LISTENER;
    }

    CopyConfig(IAction actionOk, IAction actionFail, Long id) {
        this.actionOk = actionOk;
        this.actionFail = actionFail;
        this.id = id;
        notificationType = NotificationType.EVENT;
        asAction = true;
    }

    CopyConfig(String actionOkString, String actionFailString, Long id) {
        this.actionOkString = actionOkString;
        this.actionFailString = actionFailString;
        this.id = id;
        notificationType = NotificationType.EVENT;
        asAction = false;
    }

    public boolean isAsAction() {
        return asAction;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public ICopyListener getCopyListener() {
        return copyListener;
    }

    public IAction getActionOk() {
        return actionOk;
    }

    public IAction getActionFail() {
        return actionFail;
    }


    public String getActionFailString() {
        return actionFailString;
    }

    public String getActionOkString() {
        return actionOkString;
    }

    public static CopyConfig createListenerConfig(ICopyListener copyListener) {
        return new CopyConfig(copyListener);
    }

    public static CopyConfig createEventConfig(IAction actionOk, IAction actionFail, Long id) {
        return new CopyConfig(actionOk, actionFail, id);
    }

    public static CopyConfig createEventConfig(IAction actionOk, IAction actionFail) {
        return new CopyConfig(actionOk, actionFail, null);
    }

    public static CopyConfig createEventConfig(String actionOk, String actionFail, Long id) {
        return new CopyConfig(actionOk, actionFail, id);
    }

    public static CopyConfig createEventConfig(String actionOk, String actionFail) {
        return new CopyConfig(actionOk, actionFail, null);
    }


    public Long getId() {
        return id;
    }
}
