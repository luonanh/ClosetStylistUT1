package com.adl.closetstylist.ui;
 
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateUtils;

import com.adl.closetstylist.R;
import com.adl.closetstylist.location.MockLocationProvider;
 
public class SplashScreen extends Activity implements LocationListener {
 
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 1500;
 
	// Location variables
	private LocationManager mLocationManager;
	private Location mLastLocationReading;
	// A fake location provider used for testing, used with mMockLocationProvider.pushLocation(37.422, -122.084);
	private MockLocationProvider mMockLocationProvider;
	// default minimum time between new location readings
	private long mMinTime = 5000;
	// default minimum distance between old and new readings.
	private float mMinDistance = 1000.0f;
	private final long FIVE_MINS = 5 * 60 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
 
        new Handler().postDelayed(new Runnable() {
 
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
 
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(i);
 
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

	@Override
	protected void onResume() {
		super.onResume();
		
		mMockLocationProvider = new MockLocationProvider(
				LocationManager.NETWORK_PROVIDER, this);

		// Check NETWORK_PROVIDER for an existing location reading.
		// Only keep this last reading if it is NOT obtained today.
		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		List<String> matchingProviders = mLocationManager.getAllProviders();
		Location newLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		for (String provider : matchingProviders) {
			newLocation = mLocationManager.getLastKnownLocation(provider);
			if (null != newLocation) {
				break;
			}
		}
		
		if (null != newLocation) {
			if ((null == mLastLocationReading) || ((null != mLastLocationReading) 
					&& (!DateUtils.isToday(mLastLocationReading.getTime())))) {
				mLastLocationReading = newLocation;
			}
			
		}
		
		// Register to receive location updates from NETWORK_PROVIDER
		mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, mMinTime, mMinDistance, this);
	}
	
	@Override
	protected void onPause() {
		mMockLocationProvider.shutdown();
		// Unregister for location updates
		mLocationManager.removeUpdates(this);
		super.onPause();
	}

	@Override
	public void onLocationChanged(Location currentLocation) {
		// Handle location updates
		// Cases to consider
		// 1) If there is no last location, keep the current location.
		// 2) If the current location is older than the last location, ignore
		// the current location
		// 3) If the current location is newer than the last locations, keep the
		// current location.
		if (null == mLastLocationReading) {
			mLastLocationReading = currentLocation; // case 1
		} else {
			if (age(currentLocation) < age(mLastLocationReading)) {
				mLastLocationReading = currentLocation; // case 2
			}
		}
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	
	private long age(Location location) {
		return System.currentTimeMillis() - location.getTime();
	}
 
}