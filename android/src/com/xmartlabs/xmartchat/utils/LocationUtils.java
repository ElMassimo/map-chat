/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xmartlabs.xmartchat.utils;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import com.xmartlabs.xmartchat.io.ConnectionHelper;
import com.xmartlabs.xmartchat.ui.SendLocationTask;

/**
 *
 * @author maximo
 */
public class LocationUtils {

    public static void setupLocationManager(Context context) {
    	LocationManager lm = (LocationManager)context.getSystemService(Activity.LOCATION_SERVICE);
    	lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                ConnectionHelper.sendLocation(location);
            }

            @Override
            public void onProviderDisabled(String provider) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onProviderEnabled(String provider) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                // TODO Auto-generated method stub
            }
    	});
    }
}
