package com.aizenberg.support.fsm;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.aizenberg.support.fsm.model.Alias;
import com.aizenberg.support.utils.GenericUtils;

/**
 * Created by Yuriy Aizenberg
 */
public class DefaultSwitcher implements ISwitcher {

    private Activity activity;
    private int resourceId;
    private static final CurrentFragmentAnimator FRAGMENT_ANIMATOR = new CurrentFragmentAnimator();


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



            switch (FRAGMENT_ANIMATOR.state) {
                case UNSET:
                    break;
                case DEFAULT:
                    if (FRAGMENT_ANIMATOR.isDefaultAnimationSetup()) {
                        if (FRAGMENT_ANIMATOR.isDefaultReversePresented()) {
                            fragmentTransaction.setCustomAnimations(
                                    FRAGMENT_ANIMATOR.defaultInAnimation,
                                    FRAGMENT_ANIMATOR.defaultOutAnimation,
                                    FRAGMENT_ANIMATOR.reverseDefaultInAnimation,
                                    FRAGMENT_ANIMATOR.reverseDefaultOutAnimation);
                        } else {
                            fragmentTransaction.setCustomAnimations(
                                    FRAGMENT_ANIMATOR.defaultInAnimation,
                                    FRAGMENT_ANIMATOR.defaultOutAnimation);
                        }
                    }
                    break;
                case CUSTOM:
                    if (FRAGMENT_ANIMATOR.isCustomAnimationSetup()) {
                        if (FRAGMENT_ANIMATOR.isCustomReversePresented()) {
                            fragmentTransaction.setCustomAnimations(
                                    FRAGMENT_ANIMATOR.customInAnimation,
                                    FRAGMENT_ANIMATOR.customOutAnimation,
                                    FRAGMENT_ANIMATOR.reverseCustomInAnimation,
                                    FRAGMENT_ANIMATOR.reverseCustomOutAnimation);
                        } else {
                            fragmentTransaction.setCustomAnimations(
                                    FRAGMENT_ANIMATOR.customInAnimation,
                                    FRAGMENT_ANIMATOR.customOutAnimation);
                        }
                    }
                    FRAGMENT_ANIMATOR.state = GenericUtils.orElse(FRAGMENT_ANIMATOR.previousState, CurrentFragmentAnimator.State.UNSET);
                    break;
                case TEMPORARY_DISABLED:
                    FRAGMENT_ANIMATOR.state = GenericUtils.orElse(FRAGMENT_ANIMATOR.previousState, CurrentFragmentAnimator.State.UNSET);
                    break;
            }

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
    public ISwitcher clearAnimation() {
        FRAGMENT_ANIMATOR.reset();
        FRAGMENT_ANIMATOR.state = CurrentFragmentAnimator.State.UNSET;
        return this;
    }

    @Override
    public ISwitcher setAnimations(int inAnimResource, int outAnimResource) {
        FRAGMENT_ANIMATOR.defaultInAnimation = inAnimResource;
        FRAGMENT_ANIMATOR.defaultOutAnimation = outAnimResource;
        FRAGMENT_ANIMATOR.state = CurrentFragmentAnimator.State.DEFAULT;
        return this;
    }

    @Override
    public ISwitcher setAnimations(int inAnimResource, int outAnimResource, int reverseInAnimResource, int reverseOutAnimResource) {
        FRAGMENT_ANIMATOR.reverseDefaultInAnimation = reverseInAnimResource;
        FRAGMENT_ANIMATOR.reverseDefaultOutAnimation = reverseOutAnimResource;
        return setAnimations(inAnimResource, outAnimResource);
    }

    @Override
    public ISwitcher withAnimations(int inAnimResource, int outAnimResource) {
        FRAGMENT_ANIMATOR.customInAnimation = inAnimResource;
        FRAGMENT_ANIMATOR.customOutAnimation = outAnimResource;
        FRAGMENT_ANIMATOR.previousState = FRAGMENT_ANIMATOR.state;
        FRAGMENT_ANIMATOR.state = CurrentFragmentAnimator.State.CUSTOM;
        return this;
    }

    @Override
    public ISwitcher withAnimations(int inAnimResource, int outAnimResource, int reverseInAnimResource, int reverserOutAnimResource) {
        FRAGMENT_ANIMATOR.reverseCustomInAnimation = reverseInAnimResource;
        FRAGMENT_ANIMATOR.reverseCustomOutAnimation = reverserOutAnimResource;
        return withAnimations(inAnimResource, outAnimResource);
    }

    @Override
    public ISwitcher withoutAnimation() {
        FRAGMENT_ANIMATOR.previousState = FRAGMENT_ANIMATOR.state;
        FRAGMENT_ANIMATOR.state = CurrentFragmentAnimator.State.TEMPORARY_DISABLED;
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

    private static class CurrentFragmentAnimator {
        private static final int NO_ANIM = -1;

        int defaultInAnimation = NO_ANIM;
        int defaultOutAnimation = NO_ANIM;

        int reverseDefaultInAnimation = NO_ANIM;
        int reverseDefaultOutAnimation = NO_ANIM;

        int customInAnimation = NO_ANIM;
        int customOutAnimation = NO_ANIM;

        int reverseCustomInAnimation = NO_ANIM;
        int reverseCustomOutAnimation = NO_ANIM;

        State state = State.UNSET;
        State previousState;

        public boolean isDefaultAnimationSetup() {
            return bothSetup(defaultInAnimation, defaultOutAnimation);
        }

        public boolean isCustomAnimationSetup() {
            return bothSetup(customInAnimation, customOutAnimation);
        }


        private boolean bothSetup(int first, int second) {
            return first != -1 && second != NO_ANIM;
        }

        public void reset() {
            defaultOutAnimation = defaultInAnimation = -1;
        }

        private boolean isCustomReversePresented() {
            return bothSetup(reverseCustomInAnimation, reverseCustomOutAnimation);
        }

        private boolean isDefaultReversePresented() {
            return bothSetup(reverseDefaultInAnimation, reverseDefaultOutAnimation);
        }

        enum State {
            UNSET,
            DEFAULT,
            CUSTOM,
            TEMPORARY_DISABLED
        }

    }
}
