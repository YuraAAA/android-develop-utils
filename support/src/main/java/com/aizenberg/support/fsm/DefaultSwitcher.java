package com.aizenberg.support.fsm;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.aizenberg.support.fsm.model.Alias;

/**
 * Created by Yuriy Aizenberg
 */
public class DefaultSwitcher implements ISwitcher {

    private Activity activity;
    private int resourceId;

    DefaultSwitcher(Activity activity, int resourceId) {
        this.activity = activity;
        this.resourceId = resourceId;
    }

    @Override
    public void switchTo(ISwitchable switchable, Bundle args, boolean addToBackStack) {
        switchTo(switchable.getFragmentClass(), args, addToBackStack);
    }

    @Override
    public void switchTo(ISwitchable switchable, Bundle args) {
        switchTo(switchable, args, true);
    }

    @Override
    public void switchTo(ISwitchable switchable, boolean addToBackStack) {
        switchTo(switchable, null, addToBackStack);
    }

    @Override
    public void switchTo(ISwitchable switchable) {
        switchTo(switchable, null, true);
    }

    @Override
    public void switchTo(Class<? extends Fragment> clazz, Bundle args, boolean addToBackStack) {
        if (activity != null && !activity.isFinishing()) {
            FragmentManager manager = activity.getFragmentManager();
            FragmentTransaction fragmentTransaction = manager.beginTransaction();

            String name = clazz.getName();
            Fragment fragment = Fragment.instantiate(activity, name, args);

            fragmentTransaction.replace(resourceId, fragment);
            if (addToBackStack) fragmentTransaction.addToBackStack(name);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    @Override
    public void switchTo(Class<? extends Fragment> clazz, Bundle args) {
        switchTo(clazz, args, true);
    }

    @Override
    public void switchTo(Class<? extends Fragment> clazz, boolean addToBackStack) {
        switchTo(clazz, null, addToBackStack);
    }

    @Override
    public void switchTo(Class<? extends Fragment> clazz) {
        switchTo(clazz, null, true);
    }

    @Override
    public void switchTo(String aliasName, Bundle args, boolean addToBackStack) {
        switchTo(AliasHolder.getAliasClass(aliasName), args, addToBackStack);
    }

    @Override
    public void switchTo(String aliasName, Bundle args) {
        switchTo(aliasName, args, false);
    }

    @Override
    public void switchTo(String aliasName, boolean addToBackStack) {
        switchTo(aliasName, null, addToBackStack);
    }

    @Override
    public void switchTo(String aliasName) {
        switchTo(aliasName, null, true);
    }

    @Override
    public boolean overrideBack() {
        if (activity != null && !activity.isFinishing()) {
            Fragment fragment = activity.getFragmentManager().findFragmentById(resourceId);
            if (fragment != null && fragment instanceof IFragmentBackPressListener) {
                return ((IFragmentBackPressListener) fragment).onBackPressed();
            }
        }
        return false;
    }

    @Override
    public void getBack() {
        if (activity != null && !activity.isFinishing() && activity instanceof IActivityBackPressListener) {
            ((IActivityBackPressListener) activity).getBack();
        }
    }


    @Override
    public ISwitcher clearBackStack() {
        if (activity != null && !activity.isFinishing()) {
            FragmentManager fm = activity.getFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                fm.popBackStack(fm.getBackStackEntryAt(0).getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }
        return this;
    }

    @Override
    public void addAlias(String key, Class<? extends Fragment> clazz) {
        AliasHolder.addAlias(key, clazz);
    }

    @Override
    public void addAlias(String key, ISwitchable switchable) {
        AliasHolder.addAlias(key, switchable.getFragmentClass());
    }

    @Override
    public void addAliases(Alias... aliases) {
        for (Alias a : aliases) {
            AliasHolder.addAlias(a.getAlias(), a.getClazz());
        }
    }
}
