package com.aizenberg.support.network;

import java.net.HttpURLConnection;

/**
 * Created by Yuriy Aizenberg
 */
public class DefaultRequestConfiguration implements INetworkRequestConfigurable {


    private static final int HTTP_OK = HttpURLConnection.HTTP_OK;
    private static final int TIME_OUT = 1000;
    private static final String SERVER = "http://www.google.com";
    private static final String GET = "GET";

    @Override
    public int getResponseCode() {
        return HTTP_OK;
    }

    @Override
    public int getRequestConnectionTimeOut() {
        return TIME_OUT;
    }

    @Override
    public String getServer() {
        return SERVER;
    }

    @Override
    public String getRequestMethod() {
        return GET;
    }
}
