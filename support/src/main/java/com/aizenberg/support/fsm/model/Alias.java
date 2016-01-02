package com.aizenberg.support.fsm.model;

import android.app.Fragment;

import com.aizenberg.support.fsm.ISwitchable;

/**
 * Created by Yuriy Aizenberg
 */
public class Alias {

    private Class<? extends Fragment> clazz;
    private String alias;

    public Alias(Class<? extends Fragment> clazz, String alias) {
        this(alias);
        this.clazz = clazz;
    }

    public Alias(ISwitchable switchable, String alias) {
        this(alias);
        this.clazz = switchable.getFragmentClass();
    }

    private Alias(String alias) {
        this.alias = alias;
    }

    public Class<? extends Fragment> getClazz() {
        return clazz;
    }

    public String getAlias() {
        return alias;
    }
}
