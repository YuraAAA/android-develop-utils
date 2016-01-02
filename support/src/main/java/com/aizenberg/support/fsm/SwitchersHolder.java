package com.aizenberg.support.fsm;

import android.app.Activity;

import com.aizenberg.support.logger.Logger;
import com.aizenberg.support.logger.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Yuriy Aizenberg
 */
class SwitchersHolder {

    private static Map<Class<? extends Activity>, ISwitcher> switcherMap = new ConcurrentHashMap<>();
    private static Logger logger = LoggerFactory.getLogger(SwitchersHolder.class);


    static void registerMnemonicSwitcher(Class<? extends Activity> clazz, ISwitcher switcher) {
        if (clazz == null) throw new SwitcherException("Class can't be null");
        if (switcher == null) throw new SwitcherException("Switcher can't be null");

        if (switcherMap.containsKey(clazz)) {
            logger.w(String.format("Switcher already registered for key %s", clazz.getSimpleName()));
        }
        switcherMap.put(clazz, switcher);
    }

    static void unregisterSwitcher(Class<? extends Activity> clazz) {
        if (clazz == null) throw new SwitcherException("Class can't be null");

        if (!switcherMap.containsKey(clazz)) {
            logger.w(String.format("Switcher not registered for key %s", clazz.getSimpleName()));
        } else {
            switcherMap.remove(clazz);
            logger.i(String.format("Switcher for %s unregistered", clazz.getSimpleName()));
        }
    }

    static ISwitcher obtainSwitcher(Class<? extends Activity> clazz) {
        if (clazz == null) throw new SwitcherException("Class can't be null");
        return switcherMap.get(clazz);
    }

}
