package com.adl.closetstylist;

import java.util.ArrayList;
import java.util.Arrays;

import android.location.Location;
import android.location.LocationManager;
import android.os.Parcel;
import android.os.Parcelable;

public class UserProfile implements Parcelable {
	private long id; // once added to database, this field will be populated8
	private String usr;
	private String pwd;
	private GenderEnum gender;
	private int zip;
	private int laundrySchedule; // 0 - weekly, 1 - biweekly, 2 - monthly, 3 - random
	private String laundryDay;
	private Location location; 
	// http://www.geonames.org/findNearbyPlaceName?username=anhpopeye&style=full&lat=30.4883997&lng=-97.7175117
	// http://api.geonames.org/postalCodeSearch?postalcode=78758&maxRows=10&username=demo
	
	
	private static ArrayList<String> laundryDayArray = DayEnum.getAllDayEnumString();

	private UserProfile(UserProfileBuilder builder) {
		this.usr = builder.usr;
		this.pwd = builder.pwd;
		this.gender = builder.gender;
		this.zip = builder.zip;
		this.laundrySchedule = builder.laundrySchedule;
		this.laundryDay = builder.laundryDay;
		if (null != builder.location) {
			this.location = builder.location;
		} else {
			Location defaultLocation = new Location(LocationManager.NETWORK_PROVIDER);
			defaultLocation.setLatitude(30.4883997);
			defaultLocation.setLongitude(-97.7175117);
			this.location = builder.location;
		}
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsr() {
		return usr;
	}

	public void setUsr(String usr) {
		this.usr = usr;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public GenderEnum getGender() {
		return gender;
	}

	public void setGender(GenderEnum gender) {
		this.gender = gender;
	}

	public int getZip() {
		return zip;
	}

	public void setZip(int zip) {
		this.zip = zip;
	}

	public int getLaundrySchedule() {
		return laundrySchedule;
	}

	public void setLaundrySchedule(int laundrySchedule) {
		this.laundrySchedule = laundrySchedule;
	}

	public String getLaundryDay() {
		return laundryDay;
	}

	public void setLaundryDay(String laundryDay) {
		this.laundryDay = laundryDay;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public static ArrayList<String> getLaundryDayArray() {
		return laundryDayArray;
	}

	public String toString() {
		return "UserProfile toString: id - " + id + " ; username - " + usr
				+ " ; password - " + pwd + " ; gender - " + gender.toString() 
				+ " ; zip - " + zip;
	}
	
	public static class UserProfileBuilder {
		private long id; // once added to database, this field will be populated
		private String usr;
		private String pwd;
		private GenderEnum gender;
		private int zip;
		private int laundrySchedule = 0; // 0 - weekly, 1 - biweekly, 2 - monthly, 3 - random
		private String laundryDay = "Saturday";
		private Location location;

		public UserProfileBuilder(String usr, String pwd, GenderEnum gender, int zip) {
			this.usr = usr;
			this.pwd = pwd;
			this.gender = gender;
			this.zip = zip;
		}
		
		public UserProfileBuilder laundrySchedule(int schedule) {
			this.laundrySchedule = schedule;
			return this;
		}
		
		public UserProfileBuilder laundryDay(String day) {
			this.laundryDay = day;
			return this;
		}
		
		public UserProfileBuilder id(long id) {
			this.id = id;
			return this;
		}
		
		public UserProfileBuilder location(Location loc) {
			this.location = loc;
			return this;
		}
		
		public UserProfile build() {
			UserProfile profile = new UserProfile(this);
			return profile;
		}
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
		dest.writeString(usr);
		dest.writeInt(gender.ordinal());
		dest.writeInt(zip);
		dest.writeInt(laundrySchedule);
		dest.writeString(laundryDay);
		location.writeToParcel(dest, flags);
	}
	
	/*
	 * Unparcelling part, ported from iRemember/StoryData 
	 */
	public static final Parcelable.Creator<UserProfile> CREATOR = new Parcelable.Creator<UserProfile>() {

		@Override
		public UserProfile createFromParcel(Parcel source) {
			return new UserProfile(source);
		}

		@Override
		public UserProfile[] newArray(int size) {
			return new UserProfile[size];
		}
	};
	
	private UserProfile(Parcel source) {
		id = source.readLong();
		usr = source.readString();
		pwd = source.readString();
		gender = GenderEnum.values()[source.readInt()];
		zip = source.readInt();
		laundrySchedule = source.readInt();
		laundryDay = source.readString();
		location = Location.CREATOR.createFromParcel(source);
	}
}
