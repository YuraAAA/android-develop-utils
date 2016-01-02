package com.aizenberg.support.common.async;

import android.os.AsyncTask;

/**
 * Created by Yuriy Aizenberg
 */
public abstract class CommonAsyncTask<R> extends AsyncTask<Void, Void, R> {

    private IAsyncCallback<R> callback;
    private Throwable throwable;

    public CommonAsyncTask(IAsyncCallback<R> callback) {
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (callback != null) callback.onBegin();
    }


    @Override
    protected final R doInBackground(Void... params) {
        try {
            return doAction();
        } catch (Throwable throwable) {
            this.throwable = throwable;
            return null;
        }
    }

    public abstract R doAction() throws Throwable;

    @Override
    protected void onPostExecute(R r) {
        super.onPostExecute(r);
        if (callback != null) {
            callback.onEnd();
            if (throwable != null) {
                callback.onFailure(throwable);
            } else if (r != null || isNullResultAcceptable()) {
                callback.onSuccess(r);
            } else {
                callback.onFailure(new EmptyResultException("Result null not acceptable"));
            }
        }
    }

    protected boolean isNullResultAcceptable() {
        return false;
    }


}
