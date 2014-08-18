package com.adl.closetstylist.location;

import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;

import com.adl.closetstylist.PlaceRecord;
import com.adl.closetstylist.location.geonames.LocationInfoProvider;
import com.adl.closetstylist.ui.PlaceRecordContainerInterface;

public class LocationToPostalCodeTask extends AsyncTask<Location, Void, PlaceRecord> {
    // put your www.geonames.org account name here.
    private static String USERNAME = "anhpopeye";

	private HttpURLConnection mHttpUrl;
	private WeakReference<PlaceRecordContainerInterface> mParent;
	private Context context;
	private ProgressDialog dialog;
	
	public LocationToPostalCodeTask(PlaceRecordContainerInterface parent, Context context) {
		super();
		this.mParent = new WeakReference<PlaceRecordContainerInterface>(parent);
		this.dialog = new ProgressDialog(context);
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		dialog.setMessage("Obtaining Your Current Location");
		dialog.show();
	}
	
	@Override
	protected PlaceRecord doInBackground(Location... params) {
		return LocationInfoProvider.getPlaceFromPostalCode(params[0]);
	}
	
	@Override
	protected void onPostExecute(PlaceRecord result) {
		if (this.dialog.isShowing()) {
			dialog.dismiss();
		}
		
		if (null != result && null != mParent.get()) {
			mParent.get().setPlaceRecord(result);
		}
	}
}
