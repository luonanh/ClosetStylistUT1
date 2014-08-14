package com.adl.closetstylist;

import android.text.format.DateUtils;

public class CurrentWeatherInfo {

	private static WeatherInfo wi = null;
	
	public static void setCurrentWeatherInfo(WeatherInfo w) {
		wi = w;
	}
	
	public static WeatherInfo getCurrentWeatherInfo() {
		return wi;
	}
	
	public static boolean isCurrentWeatherInfoToday() {
		if (wi == null)
			return false;
		
		return DateUtils.isToday(wi.getPlaceRecord().getLocation().getTime());
	}
}
