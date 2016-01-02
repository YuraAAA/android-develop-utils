package com.aizenberg.support.geo;

import android.location.Location;

/**
 * Created by Yuriy Aizenberg
 */
public interface IGeoListener {

    void onLocationChanged(Location location, String provider);

}
