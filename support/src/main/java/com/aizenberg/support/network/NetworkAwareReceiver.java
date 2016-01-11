package com.aizenberg.support.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Yuriy Aizenberg
 */
public class NetworkAwareReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NetworkConnectionManager.getInstance().notifyConnectivityChange(getState(context));
        onReceiveNetworkStateChange();
    }

    static NetworkConnectionManager.NetworkState getState(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        if (activeNetworkInfo == null) return new NetworkConnectionManager.NetworkState(false, NetworkType.UNDEFINED);
        boolean connecting = activeNetworkInfo.isConnectedOrConnecting();
        NetworkType type = null;
        if (connecting) {
            NetworkInfo info = cm.getActiveNetworkInfo();
            if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                type = NetworkType.WIFI;
            } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                type = NetworkType.MOBILE;
            } else {
                type = NetworkType.UNDEFINED;
            }
        }
        return new NetworkConnectionManager.NetworkState(connecting, type);
    }

    protected void onReceiveNetworkStateChange() {

    }

}
