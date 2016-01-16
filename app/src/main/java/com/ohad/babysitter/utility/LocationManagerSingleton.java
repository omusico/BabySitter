package com.ohad.babysitter.utility;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.ohad.babysitter.R;
import com.ohad.babysitter.base.ApplicationBase;
import com.parse.ParseGeoPoint;

/**
 * Created by Ohad on 16/01/2016.
 *
 */
public class LocationManagerSingleton implements LocationListener {

    private static final int TWO_MINUTES = 1000 * 60 * 2;

    private Location mCurrentLocation;
    private int mDistanceKm;

    private static LocationManagerSingleton ourInstance = new LocationManagerSingleton();

    public static LocationManagerSingleton getInstance() {
        return ourInstance;
    }

    private LocationManager locationManager;
    private Context mContext;

    private LocationManagerSingleton() {
        this.mContext = ApplicationBase.getInstance();
        this.mDistanceKm = 50;

        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
    }

    public void findLocation() {
        // check if the required permissions did not granted.
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(mContext, R.string.error_fine_location_denied, Toast.LENGTH_SHORT).show();
        } else {
            // Register the listener with the Location Manager to receive location updates.
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        }
    }

    public void stop() {
        // check if the required permissions did not granted.
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(mContext, R.string.error_fine_location_denied, Toast.LENGTH_SHORT).show();
        } else {
            // stop updates.
            locationManager.removeUpdates(this);
        }
    }

    /** Determines whether one Location reading is better than the current Location fix
     * @param location  The new Location that you want to evaluate
     * @param currentBestLocation  The current Location fix, to which you want to compare the new one
     */
    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    public Location getLatestLocation() {
        return mCurrentLocation;
    }

    public ParseGeoPoint getParseGeoPoint() {
        if (mCurrentLocation == null)
            return null;

        return new ParseGeoPoint(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
    }

    public int getDistanceKm() {
        return mDistanceKm;
    }

    public void setDistanceKm(int mDistanceKm) {
        this.mDistanceKm = mDistanceKm;
    }

    @Override
    public void onLocationChanged(Location location) {
        Utility.d("onLocationChanged ### " + location.toString());
        if (isBetterLocation(location, mCurrentLocation)) {
            mCurrentLocation = location;
            stop();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Utility.d("onStatusChanged ### provider: " + provider + " , status: " + status);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Utility.d("onProviderEnabled ### " + provider);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Utility.d("onProviderDisabled ### " + provider);
    }
}
