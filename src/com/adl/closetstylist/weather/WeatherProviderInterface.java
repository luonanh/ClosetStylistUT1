package com.adl.closetstylist.weather;

import java.io.InputStream;

import com.adl.closetstylist.PlaceRecord;
import com.adl.closetstylist.WeatherInfo;

public interface WeatherProviderInterface {

	public String getWeatherDataFromLatLong(PlaceRecord place);
	public WeatherInfo getWeatherInfoFromWeatherData(String data, PlaceRecord place);
}
