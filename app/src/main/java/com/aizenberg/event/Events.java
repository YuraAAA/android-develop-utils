package com.aizenberg.event;

import com.aizenberg.support.event.IAction;

/**
 * Created by Yuriy Aizenberg
 */
public enum Events implements IAction {

    COMPUTATION("computation");

    private String key;

    Events(String key) {
        this.key = key;
    }


    @Override
    public String getAction() {
        return key;
    }
}
