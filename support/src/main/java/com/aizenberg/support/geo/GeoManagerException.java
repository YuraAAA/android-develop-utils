package com.aizenberg.support.geo;

import com.aizenberg.support.common.error.SupportException;

/**
 * Created by Yuriy Aizenberg
 */
public class GeoManagerException extends SupportException {
    public GeoManagerException() {
    }

    public GeoManagerException(String detailMessage) {
        super(detailMessage);
    }

    public GeoManagerException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public GeoManagerException(Throwable throwable) {
        super(throwable);
    }
}
