package com.aizenberg.support.collection;

import com.aizenberg.support.common.error.SupportException;

/**
 * Created by Yuriy Aizenberg
 */
public class IllegalFilterConfigureException extends SupportException {

    public IllegalFilterConfigureException() {
    }

    public IllegalFilterConfigureException(String detailMessage) {
        super(detailMessage);
    }

    public IllegalFilterConfigureException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public IllegalFilterConfigureException(Throwable throwable) {
        super(throwable);
    }
}
