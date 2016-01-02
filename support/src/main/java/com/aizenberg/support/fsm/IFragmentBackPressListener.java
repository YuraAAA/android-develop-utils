package com.aizenberg.support.fsm;

/**
 * Created by Yuriy Aizenberg
 */
public interface IFragmentBackPressListener {

    /**
     *
     * @return true, if back press override, false otherwise
     */
    boolean onBackPressed();

}
