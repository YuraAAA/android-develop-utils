package com.aizenberg.support.fsm;

import android.app.Fragment;

/**
 * Created by Yuriy Aizenberg
 */
public interface ISwitchable {

    Class<? extends Fragment> getFragmentClass();

}
