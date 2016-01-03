package com.aizenberg.fsm;

import android.app.Fragment;

import com.aizenberg.support.fsm.ISwitchable;

/**
 * Created by Yuriy Aizenberg
 */
public enum Fragments implements ISwitchable {
    ONE(OneFragment.class),
    TWO(TwoFragment.class),
    THREE(ThreeFragment.class);

    private Class<? extends Fragment> clazz;

    Fragments(Class<? extends Fragment> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Class<? extends Fragment> getFragmentClass() {
        return clazz;
    }
}
