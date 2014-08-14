package com.adl.closetstylist.ui;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.adl.closetstylist.DayEnum;
import com.adl.closetstylist.GenderEnum;
import com.adl.closetstylist.PlaceRecord;
import com.adl.closetstylist.R;
import com.adl.closetstylist.UserProfile;
import com.adl.closetstylist.db.ItemDatabaseHelper;
import com.adl.closetstylist.location.LocationToPostalCodeTask;
import com.adl.closetstylist.location.MockLocationProvider;
import com.adl.closetstylist.location.PostalCodeToLocationTask;
import com.adl.closetstylist.ui.view.ArrayAdapter;

public class RegisterActivity extends Activity implements LocationListener, PlaceRecordContainerInterface {
	private final static String TAG = RegisterActivity.class.getCanonicalName();
	
	private ItemDatabaseHelper itemDatabaseHelper;
	
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
	private PlaceRecord place; // updated by setPlaceRecord()
	
	private UserProfile userProfile;

	enum SpinnerValue {
		STYLE(R.id.style, R.array.styles),
		TOPS(R.id.tops, R.array.tops_size),
		BOTTOMS(R.id.bottoms, R.array.bottoms_size),
		DRESSES(R.id.dresses, R.array.dresses_size),
		SHOES(R.id.shoes, R.array.shoes_size),
		LAUNDRY_SCHEDULE_1(R.id.laundry_schedule_1, R.array.laundry_schedule_1),
		LAUNDRY_SCHEDULE_2(R.id.laundry_schedule_2, R.array.laundry_schedule_2),
		RELATIONSHIP_STATUS(R.id.relationship_status, R.array.relationship_status),
		FAVOURITE_COLOR(R.id.favourite_color, R.array.colors),
		HAIR_COLOR(R.id.hair_color, R.array.hair_color);

		private int arrayId;
		private int id;

		private SpinnerValue(int id, int arrayId) {
			this.id = id;
			this.arrayId = arrayId;
		}

		public int getArrayId() {
			return arrayId;
		}

		public int getId() {
			return id;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		initSpinner();
		initListener();		
		initGenderDependency();
		
		itemDatabaseHelper = new ItemDatabaseHelper(this);
		Button buttonLoc = (Button) findViewById(R.id.btn_get_location);
		buttonLoc.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				TextView zip = (TextView) findViewById(R.id.zipcode);
				String zipCode = zip.getText().toString();
				if (zipCode.equalsIgnoreCase("")) {
					zipCode = "78758";
				}
				if (null != mLastLocationReading) {
					new LocationToPostalCodeTask(RegisterActivity.this, RegisterActivity.this)
							.execute(mLastLocationReading);					
				}
			}
		});
		
		Button buttonRegister = (Button) findViewById(R.id.btn_register);
		buttonRegister.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				TextView usr = (TextView) findViewById(R.id.username);
				TextView pwd = (TextView) findViewById(R.id.password);
				TextView zip = (TextView) findViewById(R.id.zipcode);
				Location location = place.getLocation();
				UserProfile temp = new UserProfile.UserProfileBuilder(
						usr.getText().toString(), 
						pwd.getText().toString(),
						getGender(),
						Integer.parseInt(zip.getText().toString()))
						.laundrySchedule(getLaundrySchedule())
						.laundryDay(getLaundryDay())
						.location(location)
						.build();
				itemDatabaseHelper.saveUserProfileRecord(temp);
				
				// Start your app main activity when username and password match
                Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(i);
                
                // close this activity
				finish();
			}
		});
	}
	
	private GenderEnum getGender() {
		RadioGroup genderGroup = (RadioGroup) findViewById(R.id.genderGroup);
		int i = genderGroup.indexOfChild(genderGroup.findViewById(genderGroup.getCheckedRadioButtonId()));
		switch (i) {
		case 0:
			return GenderEnum.FEMALE;
		default:
			return GenderEnum.MALE;
		}
	}
	
	private void setGender(GenderEnum g) {
		RadioGroup genderGroup = (RadioGroup) findViewById(R.id.genderGroup);
		switch(g) {
		case FEMALE:
			genderGroup.check(R.id.genderGroupFemale);
			break;
		default:
			genderGroup.check(R.id.genderMGroupMale);
		}
	}
	
	private int getLaundrySchedule() {
		Spinner laundryScheduleWeekSpinner = (Spinner) findViewById(R.id.laundry_schedule_1);
		return laundryScheduleWeekSpinner.getSelectedItemPosition();
	}
	
	private void setLaundrySchedule(int i) {
		Spinner laundryScheduleWeekSpinner = (Spinner) findViewById(R.id.laundry_schedule_1);
		laundryScheduleWeekSpinner.setSelection(i);
	}
	
	private String getLaundryDay() {
		Spinner laundryScheduleDaySpinner = (Spinner) findViewById(R.id.laundry_schedule_2);
		return laundryScheduleDaySpinner.getSelectedItem().toString();
	}
	
	private void setLaundryDay(DayEnum day) {
		Spinner laundryScheduleDaySpinner = (Spinner) findViewById(R.id.laundry_schedule_2);
		laundryScheduleDaySpinner.setSelection(day.ordinal());
	}

	private void initSpinner() {
		for (SpinnerValue value : SpinnerValue.values()) {
			Spinner spinner = (Spinner) findViewById(value.getId());
			SpinnerAdapter adapter = new ArrayAdapter<String>(
					getApplicationContext(),
					R.layout.simple_spinner_dropdown_item, 
					getResources().getStringArray(value.getArrayId()));
			spinner.setAdapter(adapter);
			spinner.setSelection(adapter.getCount());
		}
	}

	private void initGenderDependency() {
		final RadioGroup genderGroup = (RadioGroup) findViewById(R.id.genderGroup);
		final View dressesSizeSpinner = findViewById(R.id.dresses);
		final TextView chest = (TextView) findViewById(R.id.chest);
		genderGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int gender) {
				switch (gender) {
				case 1: // female
					dressesSizeSpinner.setVisibility(View.VISIBLE);
					chest.setVisibility(TextView.VISIBLE);
					return;
				case 2: // male
					dressesSizeSpinner.setVisibility(View.GONE);
					chest.setVisibility(TextView.GONE);
					return;
				}
			}
		});
	}
	
	private void initListener() {
		final Spinner styleSpinner = (Spinner) findViewById(R.id.style);
		final Spinner favoriteColorSpinner = (Spinner) findViewById(R.id.favourite_color);
		final Spinner laundryScheduleWeekSpinner = (Spinner) findViewById(R.id.laundry_schedule_1);
		final Spinner laundryScheduleDaySpinner = (Spinner) findViewById(R.id.laundry_schedule_2);
		final Spinner relationshipStatusSpinner = (Spinner) findViewById(R.id.relationship_status);
		final Spinner topsSizeSpinner = (Spinner) findViewById(R.id.tops);
		final Spinner bottomsSizeSpinner = (Spinner) findViewById(R.id.bottoms);
		final Spinner dressesSizeSpinner = (Spinner) findViewById(R.id.dresses);
		final Spinner shoesSizeSpinner = (Spinner) findViewById(R.id.shoes);
		final Spinner hairColorSpinner = (Spinner) findViewById(R.id.hair_color);
		
		final EditText username = (EditText) findViewById(R.id.username);
		final EditText password = (EditText) findViewById(R.id.password);
		final EditText firstName = (EditText) findViewById(R.id.firstname);
		final EditText lastName = (EditText) findViewById(R.id.lastname);
		final EditText age = (EditText) findViewById(R.id.age);
		final EditText zipcode = (EditText) findViewById(R.id.zipcode);
		final EditText country = (EditText) findViewById(R.id.country);
		final EditText state = (EditText) findViewById(R.id.state);
		final EditText city = (EditText) findViewById(R.id.city);
		
		final EditText heightFoot = (EditText) findViewById(R.id.height_foot);
		final EditText heightInch= (EditText) findViewById(R.id.height_inch);
		final EditText weight = (EditText) findViewById(R.id.weight);
		final EditText naturalWaist = (EditText) findViewById(R.id.natural_waist);
		final EditText lowWaist = (EditText) findViewById(R.id.low_waist);
		final EditText inseam = (EditText) findViewById(R.id.inseam);
		final EditText hip = (EditText) findViewById(R.id.hip);
		final EditText chest = (EditText) findViewById(R.id.chest);
		
		final Button getLocationBtn = (Button) findViewById(R.id.btn_get_location);
		final Button resetBtn = (Button) findViewById(R.id.btn_reset);
		final Button registerBtn = (Button) findViewById(R.id.btn_register);
		
		resetBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				username.setText("");
				password.setText("");
				zipcode.setText("");
				country.setText("");
				state.setText("");
				city.setText("");
				setGender(GenderEnum.FEMALE);
				laundryScheduleWeekSpinner.setSelection(0);
				laundryScheduleDaySpinner.setSelection(0);
				place = null;
			}
		});
	}

	/**
	 * Update location related fields for this activity
	 */
	@Override
	public void setPlaceRecord(PlaceRecord place) {
		TextView zipcode = (TextView) findViewById(R.id.zipcode);
		TextView country = (TextView) findViewById(R.id.country);
		TextView state = (TextView) findViewById(R.id.state);
		TextView city = (TextView) findViewById(R.id.city);
		
		zipcode.setText(String.valueOf(place.getPostalCode()));
		country.setText(place.getCountryCode());
		state.setText(place.getStateName());
		city.setText(place.getPlaceName());
		this.place = place;
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
	
	private long age(Location location) {
		return System.currentTimeMillis() - location.getTime();
	}

	@Override
	protected void onPause() {
		mMockLocationProvider.shutdown();
		// Unregister for location updates
		mLocationManager.removeUpdates(this);
		super.onPause();
	}
	
	/*
	 * This is just for debugging only.
	 * Will be deleted afterwards.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_register, menu);
		return true;
	}

	/*
	 * This is just for debugging only.
	 * Will be deleted afterwards.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		UserProfile up;
		TextView usr = (TextView) findViewById(R.id.username);
		TextView pwd = (TextView) findViewById(R.id.password);
		TextView zip = (TextView) findViewById(R.id.zipcode);
		switch (item.getItemId()) {
		case R.id.register_menu_load_male:
			up = itemDatabaseHelper.getDefaultMaleUserProfile();
			usr.setText(up.getUsr());
			pwd.setText(up.getPwd());
			setGender(up.getGender());
			zip.setText(String.valueOf(up.getZip()));
			setLaundrySchedule(up.getLaundrySchedule());
			new PostalCodeToLocationTask(RegisterActivity.this, RegisterActivity.this)
					.execute(Integer.parseInt(zip.getText().toString()));
			return true;
		case R.id.register_menu_load_female:
			up = itemDatabaseHelper.getDefaultFemaleUserProfile();
			usr.setText(up.getUsr());
			pwd.setText(up.getPwd());
			setGender(up.getGender());
			zip.setText(String.valueOf(up.getZip()));
			setLaundrySchedule(up.getLaundrySchedule());
			new PostalCodeToLocationTask(RegisterActivity.this, RegisterActivity.this)
					.execute(Integer.parseInt(zip.getText().toString()));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
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

}