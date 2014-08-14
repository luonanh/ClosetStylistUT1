package com.adl.closetstylist;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

public class PlaceRecord implements Parcelable {
	private String countryCode;
	private String stateName;
	private String placeName;
	private int postalCode;
	private Location location;
	
	public PlaceRecord(String countryCode, String state, String place, int postal, Location location) {
		this.countryCode = countryCode;
		this.stateName = state;
		this.placeName = place;
		this.postalCode = postal;
		this.location = location;
		this.location.setTime(System.currentTimeMillis());
	}
	
	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public int getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(int postalCode) {
		this.postalCode = postalCode;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public boolean intersects(Location location) {

		double tolerance = 1000;

		return (location.distanceTo(location) <= tolerance);

	}
	
	@Override
	public String toString(){
		return "Place: " + placeName + " State: " + stateName + " Postal Code: " 
				+ postalCode + " Country: " + countryCode;
		
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(countryCode);
		dest.writeString(stateName);
		dest.writeString(placeName);
		dest.writeInt(postalCode);
		location.writeToParcel(dest, flags);
	}
	
	/*
	 * Unparcelling part, ported from iRemember/StoryData 
	 */
	public static final Parcelable.Creator<PlaceRecord> CREATOR = new Parcelable.Creator<PlaceRecord>() {

		@Override
		public PlaceRecord createFromParcel(Parcel source) {
			return new PlaceRecord(source);
		}

		@Override
		public PlaceRecord[] newArray(int size) {
			return new PlaceRecord[size];
		}
	};
	
	private PlaceRecord(Parcel source) {
		countryCode = source.readString();
		stateName = source.readString();
		placeName = source.readString();
		postalCode = source.readInt();
		location = Location.CREATOR.createFromParcel(source);
	}
}
