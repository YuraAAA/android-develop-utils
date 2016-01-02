package com.aizenberg.support.utils;

/**
 * Created by Yuriy Aizenberg
 */
public interface ICopyListener {

    void onCopyFinish();

    void onCopyFailure(Throwable cause);

}
