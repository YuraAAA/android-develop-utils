package com.aizenberg.support.geo.config;

import android.location.LocationManager;

import com.aizenberg.support.common.NotificationType;
import com.aizenberg.support.event.IAction;
import com.aizenberg.support.geo.GeoManagerException;

/**
 * Created by Yuriy Aizenberg
 */
public class LocationConfiguration {

    private NotificationType notificationType;
    private boolean notificationImmediate;
    private LocationProviderType locationProviderType;
    private float minDistance;
    private long minTime;
    private boolean lazyStart;
    private IAction action;
    private String stringAction;

    public IAction getAction() {
        return action;
    }

    public String getStringAction() {
        return stringAction;
    }

    public enum LocationProviderType {
        GPS(LocationManager.GPS_PROVIDER),
        A_GPS(LocationManager.NETWORK_PROVIDER),
        PASSIVE(LocationManager.PASSIVE_PROVIDER);
        private String provider;

        LocationProviderType(String provider) {
            this.provider = provider;
        }

        public String getProvider() {
            return provider;
        }
    }

    private LocationConfiguration() {
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public boolean isNotificationImmediate() {
        return notificationImmediate;
    }

    public LocationProviderType getLocationProviderType() {
        return locationProviderType;
    }

    public float getMinDistance() {
        return minDistance;
    }

    public long getMinTime() {
        return minTime;
    }

    public boolean isLazyStart() {
        return lazyStart;
    }

    public static class LocationConfigurationBuilder {
        private LocationConfiguration configuration;

        public LocationConfigurationBuilder() {
            this.configuration = new LocationConfiguration();
        }

        public LocationConfigurationBuilder setNotificationByListener() {
            configuration.notificationType = NotificationType.LISTENER;
            return this;
        }

        public LocationConfigurationBuilder setNotificationByEvent(IAction action) {
            configuration.notificationType = NotificationType.EVENT;
            configuration.action = action;
            return this;
        }

        public LocationConfigurationBuilder setNotificationByEvent(String action) {
            configuration.notificationType = NotificationType.EVENT;
            configuration.stringAction = action;
            return this;
        }

        public LocationConfigurationBuilder setNotificationImmediate(boolean notificationImmediate) {
            configuration.notificationImmediate = notificationImmediate;
            return this;
        }

        public LocationConfigurationBuilder setLocationProviderType(LocationProviderType providerType) {
            configuration.locationProviderType = providerType;
            return this;
        }

        public LocationConfigurationBuilder setMinDistance(float minDistance) {
            configuration.minDistance = minDistance;
            return this;
        }

        public LocationConfigurationBuilder setMinTime(long minTime) {
            configuration.minTime = minTime;
            return this;
        }

        public LocationConfigurationBuilder setLazyStart(boolean lazyStart) {
            configuration.lazyStart = lazyStart;
            return this;
        }

        public LocationConfiguration build() {
            if (configuration.getNotificationType() == NotificationType.EVENT) {
                if (configuration.stringAction == null && configuration.action == null) throw new GeoManagerException("You select notification via event, but doesn't setup key!");
            }
            return configuration;
        }

    }


}
