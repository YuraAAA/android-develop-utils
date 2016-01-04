package com.aizenberg.support.network;

/**
 * Created by Yuriy Aizenberg
 */
public interface INetworkRequestConfigurable {

    int getResponseCode();

    int getRequestConnectionTimeOut();

    String getServer();

    String getRequestMethod();

}
