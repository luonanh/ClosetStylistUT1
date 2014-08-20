package com.adl.closetstylist.ui.actionfragment;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.adl.closetstylist.CurrentWeatherInfo;
import com.adl.closetstylist.PlaceRecord;
import com.adl.closetstylist.R;
import com.adl.closetstylist.UserProfile;
import com.adl.closetstylist.WeatherInfo;
import com.adl.closetstylist.db.ItemDatabaseHelper;
import com.adl.closetstylist.location.LocationToPostalCodeTask;
import com.adl.closetstylist.ui.ActionDescriptor;
import com.adl.closetstylist.ui.MainActivity;
import com.adl.closetstylist.ui.PlaceRecordContainerInterface;
import com.adl.closetstylist.weather.OpenWeatherMapMockFeed;
import com.adl.closetstylist.weather.OpenWeatherMapProvider;
import com.adl.closetstylist.weather.WeatherProviderInterface;

public class DashboardFragment extends ActionFragment implements PlaceRecordContainerInterface {
	private final static String TAG = DashboardFragment.class.getCanonicalName();
	
	// Location constants
	private static final long ONE_MIN = 1000 * 60;
	private static final long TWO_MIN = ONE_MIN * 2;
	private static final long FIVE_MIN = ONE_MIN * 5;
	private static final long MEASURE_TIME = 1000 * 30;
	private static final long POLLING_FREQ = 1000 * 10;
	private static final float MIN_ACCURACY = 25.0f;
	private static final float MIN_LAST_READ_ACCURACY = 500.0f;
	private static final float MIN_DISTANCE = 10.0f;
	private static final String CELSIUS = "\u2103";  
	private static final String FAHRENHEIT = "\u2109";

	private ItemDatabaseHelper itemDatabaseHelper;
	private Context context;
	private WeatherInfo weatherInfo;
	private PlaceRecord place; // will use this later to hold the current location information by calling LocationToPostalCodeTask
	private UserProfile up;
	
	// Current best location estimate
	private Location mBestReading;

	// Reference to the LocationManager and LocationListener
	private LocationManager mLocationManager;
	private LocationListener mLocationListener;
	
	TextView cityState;
	TextView minTemp;
	TextView maxTemp;
	TextView currentTemp;
	TextView date;
	TextView rain;

	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	private static final String ARG_ACTION_ID = "action_id";

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public DashboardFragment() {
		super(ActionDescriptor.Dashboard);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		itemDatabaseHelper = ItemDatabaseHelper.getInstance(getActivity());
		context = getActivity().getApplicationContext();
		
		// Set up the action bar.
		final ActionBar actionBar = getActivity().getActionBar();
		actionBar.setNavigationMode(actionDescriptor.getActionBarNavigationMode());
				
		View rootView = inflater.inflate(R.layout.fragment_dashboard,
				container, false);

		View outfitOfTheDay = rootView.findViewById(R.id.action_outfitoftheday);
		outfitOfTheDay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ActionDescriptor actionDescriptor = ActionDescriptor.OutfitOfTheDay;
				Fragment actionFragment = ActionFragment.getNewActionFragment(actionDescriptor);
				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction()
						.replace(R.id.container, actionFragment).addToBackStack(null).commit();
			}
		});
		
		View myCloset = rootView.findViewById(R.id.action_mycloset);
		myCloset.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ActionDescriptor actionDescriptor = ActionDescriptor.MyCloset;
				Fragment actionFragment = ActionFragment.getNewActionFragment(actionDescriptor);
				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction()
						.replace(R.id.container, actionFragment).addToBackStack(null).commit();
			}
		});
		
		View myLaundryBag = rootView.findViewById(R.id.action_mylaundrybag);
		myLaundryBag.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ActionDescriptor actionDescriptor = ActionDescriptor.MyLaundryBag;
				Fragment actionFragment = ActionFragment.getNewActionFragment(actionDescriptor);
				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction()
						.replace(R.id.container, actionFragment).addToBackStack(null).commit();
			}
		});
		
		View myOutfitHistory = rootView.findViewById(R.id.action_outfithistory);
		myOutfitHistory.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ActionDescriptor actionDescriptor = ActionDescriptor.OutfitHistory;
				Fragment actionFragment = ActionFragment.getNewActionFragment(actionDescriptor);
				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction()
						.replace(R.id.container, actionFragment).addToBackStack(null).commit();
			}
		});
		
		cityState = (TextView) rootView.findViewById(R.id.weather_location);
		minTemp = (TextView) rootView.findViewById(R.id.weather_min_temp);
		maxTemp = (TextView) rootView.findViewById(R.id.weather_max_temp);
		currentTemp = (TextView) rootView.findViewById(R.id.weather_current_temp);
		date = (TextView) rootView.findViewById(R.id.weather_date);
		date.setText(DateFormat.getDateInstance().format(new Date()));
		rain = (TextView) rootView.findViewById(R.id.weather_type);

		// Acquire reference to the LocationManager
		if (null == (mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE)))
			getActivity().finish();

		// Get best last location measurement
		mBestReading = bestLastKnownLocation(MIN_LAST_READ_ACCURACY, FIVE_MIN);

		// Display last reading information
		if (null != mBestReading) {
			//updateDisplay(mBestReading);
		} else {
			cityState.setText("Cannot find current location");
		}

		mLocationListener = new LocationListener() {

			// Called back when location changes
			public void onLocationChanged(Location location) {
				// Determine whether new location is better than current best
				// estimate
				if (null == mBestReading
						|| location.getAccuracy() < mBestReading.getAccuracy()) {

					// Update best estimate
					mBestReading = location;

					// Update display
					//updateDisplay(location);

					if (mBestReading.getAccuracy() < MIN_ACCURACY)
						mLocationManager.removeUpdates(mLocationListener);
				}
			}

			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// NA
			}

			public void onProviderEnabled(String provider) {
				// NA
			}

			public void onProviderDisabled(String provider) {
				// NA
			}
		};

		// If there's no weather information for today yet, let's get it.
		if (!CurrentWeatherInfo.isCurrentWeatherInfoToday()) {
			new LocationToPostalCodeTask(DashboardFragment.this, getActivity())
					.execute(mBestReading);				
		} else {
			updateDisplay(CurrentWeatherInfo.getCurrentWeatherInfo());
		}

		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments().getInt(
				ARG_ACTION_ID));
	}

	@Override
	public void onResume() {
		super.onResume();
		((MainActivity)getActivity()).onSectionAttached(getArguments().getInt(
				ARG_ACTION_ID));
		
		// Determine whether initial reading is
		// "good enough"
		if (mBestReading.getAccuracy() > MIN_LAST_READ_ACCURACY
				|| mBestReading.getTime() < System.currentTimeMillis()
						- TWO_MIN) {

			// Register for network location updates
			mLocationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, POLLING_FREQ, MIN_DISTANCE,
					mLocationListener);

			// Register for GPS location updates
			mLocationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, POLLING_FREQ, MIN_DISTANCE,
					mLocationListener);

			// Schedule a runnable to unregister location listeners
			Executors.newScheduledThreadPool(1).schedule(new Runnable() {
				@Override
				public void run() {
					Log.i(TAG, "location updates cancelled");
					mLocationManager.removeUpdates(mLocationListener);
				}
			}, MEASURE_TIME, TimeUnit.MILLISECONDS);
		}

	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		// Unregister location listeners
		mLocationManager.removeUpdates(mLocationListener);
	}

	
	private class WeatherServiceTask extends AsyncTask<PlaceRecord, Void, WeatherInfo>{
		private WeatherProviderInterface weatherProvider; // an interface can be swapped at run time
		private ProgressDialog dialog;
		// Change to false if you don't have network access
		private static final boolean HAS_NETWORK = false;
		
		public WeatherServiceTask(WeatherProviderInterface wp) {
			super();
			weatherProvider = wp;
			
			// get exception when passed in context to the ProgressDialog
			// constructor. Change to OutfitActivity.this per 
			// http://stackoverflow.com/questions/19024940/android-error-unable-to-add-window-token-null-is-not-for-an-application
			dialog = new ProgressDialog(DashboardFragment.this.getActivity());
		}

		@Override
		protected void onPreExecute() {
			dialog.setMessage("Obtaining WeatherInfo");
			dialog.show();
		}

		@Override
		protected WeatherInfo doInBackground(PlaceRecord... params) {
			if (HAS_NETWORK) {
				String data = weatherProvider.getWeatherDataFromLatLong(params[0]);
				return weatherProvider.getWeatherInfoFromWeatherData(data, place);				
			} else {
				// use mock to avoid reaching the query limit of OpenWeatherMap 
				return weatherProvider.getWeatherInfoFromWeatherData(OpenWeatherMapMockFeed.rawText(), place);				
			}
		}

		@Override
		protected void onPostExecute(WeatherInfo result) {
			// Hide dialog
			if (this.dialog.isShowing()) {
				dialog.dismiss();
			}

			if (null != result) {
				weatherInfo = result;
				updateDisplay(weatherInfo);
				CurrentWeatherInfo.setCurrentWeatherInfo(weatherInfo); // Update CurrentWeatherInfo
			} else {
				Toast.makeText(context, R.string.outfit_message_no_weather_info, 
						Toast.LENGTH_SHORT).show();
			}
		}		
	}

	@Override
	public void setPlaceRecord(PlaceRecord newPlace) {
		if (newPlace != null) {
			place = newPlace;
			cityState.setText(place.getPlaceName() + ", " + place.getStateName());
			WeatherServiceTask task = new WeatherServiceTask(new OpenWeatherMapProvider());
			task.execute(this.place);
		}
	}
	
	// Get the last known location from all providers
	// return best reading is as accurate as minAccuracy and
	// was taken no longer then minTime milliseconds ago
	private Location bestLastKnownLocation(float minAccuracy, long minTime) {

		Location bestResult = null;
		float bestAccuracy = Float.MAX_VALUE;
		long bestTime = Long.MIN_VALUE;

		List<String> matchingProviders = mLocationManager.getAllProviders();

		for (String provider : matchingProviders) {
			Location location = mLocationManager.getLastKnownLocation(provider);

			if (location != null) {
				float accuracy = location.getAccuracy();
				long time = location.getTime();

				if (accuracy < bestAccuracy) {
					bestResult = location;
					bestAccuracy = accuracy;
					bestTime = time;
				}
			}
		}

		// Return best reading or null
		if (bestAccuracy > minAccuracy || bestTime < minTime) {
			return null;
		} else {
			return bestResult;
		}
	}

	private void updateDisplay(WeatherInfo wi) {
		place = wi.getPlaceRecord();
		cityState.setText(place.getPlaceName() + ", " + place.getStateName());
		minTemp.setText(String.valueOf(wi.getTempMin()));
		maxTemp.setText(String.valueOf(wi.getTempMax()));
		currentTemp.setText(String.valueOf(wi.getTempCurrent()) + FAHRENHEIT);
		if (wi.getRainAmount() > 3.0) {
			rain.setText("Heavy Rain");
		} else if (wi.getRainAmount() > 0) {
			rain.setText("Showers");
		} else {
			rain.setText("Sunny");
		}
	}
}