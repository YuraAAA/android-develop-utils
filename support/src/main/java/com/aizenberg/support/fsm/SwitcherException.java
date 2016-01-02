package com.aizenberg.support.fsm;

import com.aizenberg.support.common.error.SupportException;

/**
 * Created by Yuriy Aizenberg
 */
public class SwitcherException extends SupportException {

    public SwitcherException() {
    }

    public SwitcherException(String detailMessage) {
        super(detailMessage);
    }

    public SwitcherException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public SwitcherException(Throwable throwable) {
        super(throwable);
    }
}
