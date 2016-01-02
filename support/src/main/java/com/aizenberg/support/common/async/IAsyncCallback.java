package com.aizenberg.support.common.async;

/**
 * Created by Yuriy Aizenberg
 */
public interface IAsyncCallback<R> {

    void onBegin();

    void onEnd();

    void onSuccess(R result);

    void onFailure(Throwable cause);


}
