package com.aizenberg.fsm;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;

import com.aizenberg.R;
import com.aizenberg.support.collection.ITraversable;
import com.aizenberg.support.collection.TraversFilter;
import com.aizenberg.support.collection.TraversFilterOperation;
import com.aizenberg.support.collection.TraversPair;
import com.aizenberg.support.collection.Traverser;
import com.aizenberg.support.collection.filter.TraversKVFilter;
import com.aizenberg.support.collection.filter.TraversOneOfKVFilter;
import com.aizenberg.support.common.error.SupportException;
import com.aizenberg.support.event.EventBus;
import com.aizenberg.support.event.IEventReceiver;
import com.aizenberg.support.fsm.IActivityBackPressListener;
import com.aizenberg.support.fsm.Switcher;
import com.aizenberg.support.fsm.model.Alias;
import com.aizenberg.support.geo.IGeoListener;
import com.aizenberg.support.geo.SupportLocationManager;
import com.aizenberg.support.geo.config.LocationConfiguration;
import com.aizenberg.support.logger.LogLevel;
import com.aizenberg.support.logger.Logger;
import com.aizenberg.support.logger.LoggerEnvironment;
import com.aizenberg.support.logger.LoggerFactory;
import com.aizenberg.support.validation.IValidatable;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity implements IActivityBackPressListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapUsage();
        Logger logger = LoggerFactory.getLogger(MainActivity.class);

        logger.i("Hello");
        logger.w("Warning message");
        logger.e("Oops, something goes wrong", new SupportException("Test exception"));

    }


    public void mapUsage() {

        LocationConfiguration.LocationConfigurationBuilder builder = new LocationConfiguration.LocationConfigurationBuilder();
        builder
                //Auto start after #init method
                .setLazyStart(false)
                //Invoke onLocationChanged method immediate after #addListener
                .setNotificationImmediate(true)
                //Choose your provider
                .setLocationProviderType(LocationConfiguration.LocationProviderType.GPS)
                //Minimal distance to change loc
                .setMinDistance(20f)
                //Minimal time to change loc
                .setMinTime(0)
                //Notification type
                .setNotificationByEvent("location");

        SupportLocationManager.init(this, builder.build());



        SupportLocationManager.getInstance().addListener(new IGeoListener() {
            @Override
            public void onLocationChanged(Location location, String provider) {

            }
        });

        EventBus.getBus().addListener("location", new IEventReceiver() {
            @Override
            public void onReceiveAction(String action, Object... args) {
                Location location = (Location) args[0];
                new Long(1);
            }
        });

//        SupportLocationManager.getInstance().onStart();


    }

    private void registerAliases() {
        Switcher.obtainSwitcher(this).addAliases(
                new Alias(OneFragment.class, "one"),
                new Alias(TwoFragment.class, "two"),
                new Alias(ThreeFragment.class, "three")
        );
    }

    @Override
    public void onBackPressed() {
        if (!Switcher.obtainSwitcher(this).overrideBack())
            getBack();

    }

    @Override
    public void getBack() {
        super.onBackPressed();
    }
}
