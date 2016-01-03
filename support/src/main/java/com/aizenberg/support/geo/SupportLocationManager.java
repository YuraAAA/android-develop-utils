package com.aizenberg.support.geo;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.aizenberg.support.common.collection.NullExcludeSafetyList;
import com.aizenberg.support.event.EventBus;
import com.aizenberg.support.geo.config.LocationConfiguration;

import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * Created by Yuriy Aizenberg
 */
public class SupportLocationManager implements LifecycleHook {

    private LocationManager locationManager;
    private LocationConfiguration configuration;
    private static SupportLocationManager instance;
    private List<IGeoListener> geoListeners = new NullExcludeSafetyList<>();
    private Location lastKnownLocation;
    private Context context;

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            lastKnownLocation = new Location(location);
            notificationLocationChanged();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private SupportLocationManager() {
    }

    public static SupportLocationManager getInstance() {
        if (instance == null) throw new GeoManagerException("Call #init method first!");
        return instance;
    }


    public static void init(Context context, LocationConfiguration configuration) {
        if (context == null) throw new GeoManagerException("Context can't be null");
        if (configuration == null) throw new GeoManagerException("Configuration can't be null");

        instance = new SupportLocationManager();
        instance.context = context;
        instance.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        instance.configuration = configuration;

        if (!configuration.isLazyStart()) {
            instance.startInternal();
        }
    }

    public Location getLastKnownLocation() {
        return lastKnownLocation;
    }

    public void addListener(IGeoListener geoListener) {
        if (!geoListeners.contains(geoListener)) {
            geoListeners.add(geoListener);
        }

        if (configuration.isNotificationImmediate() && lastKnownLocation != null && geoListener != null) {

            geoListener.onLocationChanged(
                    lastKnownLocation,
                    configuration.getLocationProviderType().getProvider());
        }
    }

    public void removeListener(IGeoListener geoListener) {
        if (geoListeners.contains(geoListener)) geoListeners.remove(geoListener);
    }

    @Override
    public void onStart() {
        startInternal();
    }

    @Override
    public void onStop() {
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) != PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
            return;
        }
        locationManager.removeUpdates(locationListener);
    }


    private void startInternal() {
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) != PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
            return;
        }

        locationManager.requestLocationUpdates(configuration.getLocationProviderType().getProvider(),
                configuration.getMinTime(),
                configuration.getMinDistance(),
                locationListener);
    }

    private void notificationLocationChanged() {
        if (lastKnownLocation != null) {
            String provider = configuration.getLocationProviderType().getProvider();
            switch (configuration.getNotificationType()) {
                case LISTENER:
                    for (IGeoListener listener : geoListeners) {
                        listener.onLocationChanged(lastKnownLocation, provider);
                    }
                    break;
                case EVENT:
                    if (configuration.getAction() != null) {
                        EventBus.getBus().notifyAction(configuration.getAction(), lastKnownLocation, provider);
                    } else {
                        EventBus.getBus().notifyAction(configuration.getStringAction(), lastKnownLocation, provider);
                    }
                    break;
            }
        }
    }


}
