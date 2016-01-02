package com.aizenberg.support.fsm;

import android.app.Activity;

/**
 * Created by Yuriy Aizenberg
 */
public class Switcher {


    public static ISwitcher createSwitcher(Activity activity, int fragmentContainerId) {
        if (activity == null) throw new SwitcherException("Activity can't be null");
        return createSwitcher(activity, new DefaultSwitcher(activity, fragmentContainerId));
    }

    public static ISwitcher createSwitcher(Activity activity, ISwitcher switcher) {
        if (activity == null) throw new SwitcherException("Activity can't be null");
        if (switcher == null) throw new SwitcherException("Switcher can't be null");

        SwitchersHolder.registerMnemonicSwitcher(activity.getClass(), switcher);
        return switcher;
    }

    public static void unregisterSwitcher(Activity activity) {
        if (activity == null) throw new SwitcherException("Activity can't be null");
        unregisterSwitcher(activity.getClass());

    }


    public static void unregisterSwitcher(Class<? extends Activity> clazz) {
        if (clazz == null) throw new SwitcherException("Class can't be null");
        SwitchersHolder.unregisterSwitcher(clazz);

    }

    public static ISwitcher obtainSwitcher(Activity activity) {
        if (activity == null) throw new SwitcherException("Activity can't be null");
        return obtainSwitcher(activity.getClass());
    }

    public static ISwitcher obtainSwitcher(Class<? extends Activity> clazz) {
        if (clazz == null) throw new SwitcherException("Class can't be null");
        return SwitchersHolder.obtainSwitcher(clazz);
    }

}
