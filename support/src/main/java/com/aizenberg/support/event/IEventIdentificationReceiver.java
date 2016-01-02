package com.aizenberg.support.event;

/**
 * Created by Yuriy Aizenberg
 */
public interface IEventIdentificationReceiver extends IEventReceiver {

    long ANY = -1L;

    Long getIdentifier();

}
