package com.aizenberg.support.fsm;

import android.app.Fragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yuriy Aizenberg
 */
public class AliasHolder {

    private static Map<String, Class<? extends Fragment>> aliasMap = new HashMap<>();

    static void addAlias(String key, Class<? extends Fragment> aliasClass) {
        aliasMap.put(key, aliasClass);
    }

    static Class<? extends Fragment> getAliasClass(String key) {
        return aliasMap.get(key);
    }

}
