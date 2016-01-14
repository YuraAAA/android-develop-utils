package com.aizenberg.support.fsm;

import android.app.Fragment;
import android.os.Bundle;

import com.aizenberg.support.fsm.model.Alias;

/**
 * Created by Yuriy Aizenberg
 */
public interface ISwitcher {

    void switchTo(ISwitchable switchable, Bundle args, boolean addToBackStack);

    void switchTo(ISwitchable switchable, Bundle args);

    void switchTo(ISwitchable switchable, boolean addToBackStack);

    void switchTo(ISwitchable switchable);

    void switchTo(Class<? extends Fragment> clazz, Bundle args, boolean addToBackStack);

    void switchTo(Class<? extends Fragment> clazz, Bundle args);

    void switchTo(Class<? extends Fragment> clazz, boolean addToBackStack);

    void switchTo(Class<? extends Fragment> clazz);

    void switchTo(String aliasName, Bundle args, boolean addToBackStack);

    void switchTo(String aliasName, Bundle args);

    void switchTo(String aliasName, boolean addToBackStack);

    void switchTo(String aliasName);

    boolean overrideBack();

    void getBack();

    ISwitcher clearBackStack();

    ISwitcher clearAnimation();

    ISwitcher setAnimations(int inAnimResource, int outAnimResource);

    ISwitcher setAnimations(int inAnimResource, int outAnimResource, int reverseInAnimResource, int reverseOutAnimResource);

    ISwitcher withAnimations(int inAnimResource, int outAnimResource);

    ISwitcher withAnimations(int inAnimResource, int outAnimResource, int reverseInAnimResource, int reverserOutAnimResource);

    ISwitcher withoutAnimation();

    void addAlias(String key, Class<? extends Fragment> clazz);

    void addAlias(String key, ISwitchable switchable);

    void addAliases(Alias... aliases);

}
