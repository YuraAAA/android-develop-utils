package com.aizenberg.support.network;

/**
 * Created by Yuriy Aizenberg
 */
public interface NetworkConnectionAware {

    void onNetworkStateChange(boolean connected, NetworkType networkType);

}
