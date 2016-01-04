package com.aizenberg.fsm;

import android.app.Activity;
import android.os.Bundle;

import com.aizenberg.R;
import com.aizenberg.support.event.EventBus;
import com.aizenberg.support.event.IEventReceiver;
import com.aizenberg.support.network.NetworkConnectionAware;
import com.aizenberg.support.network.NetworkConnectionManager;
import com.aizenberg.support.network.NetworkType;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        NetworkConnectionManager.getInstance().addListener(new NetworkConnectionAware() {
            @Override
            public void onNetworkStateChange(boolean connected, NetworkType networkType) {
                new Long(1);
            }
        });

        NetworkConnectionManager.getInstance().setEventActionKey("network");
        EventBus.getBus().addListener("network", new IEventReceiver() {
            @Override
            public void onReceiveAction(String action, Object... args) {
                new Long(1);
            }
        });

        NetworkConnectionManager.getInstance().start();


    }

}
