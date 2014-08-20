package com.adl.closetstylist.weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.adl.closetstylist.PlaceRecord;
import com.adl.closetstylist.WeatherInfo;

import android.util.Log;

public class OpenWeatherMapProvider implements WeatherProviderInterface {
	private final static String LOG_TAG = OpenWeatherMapProvider.class.getCanonicalName();
	
	public OpenWeatherMapProvider() {
	}

	public static OpenWeatherMapWeather getWeather(String data) throws JSONException  {
		OpenWeatherMapWeather weather = new OpenWeatherMapWeather();
		try {
			// We create out JSONObject from the data
			JSONObject jObj = new JSONObject(data);
			
			// We start extracting the info		
			JSONObject coordObj = getObject("coord", jObj);
			weather.location.setLatitude(getFloat("lat", coordObj));
			weather.location.setLongitude(getFloat("lon", coordObj));
			
			JSONObject sysObj = getObject("sys", jObj);
			weather.location.setCountry(getString("country", sysObj));
			weather.location.setSunrise(getInt("sunrise", sysObj));
			weather.location.setSunset(getInt("sunset", sysObj));
			weather.location.setCity(getString("name", jObj));
			
			// We get weather info (This is an array)
			JSONArray jArr = jObj.getJSONArray("weather");
			
			// We use only the first value
			JSONObject JSONWeather = jArr.getJSONObject(0);
			weather.currentCondition.setWeatherId(getInt("id", JSONWeather));
			weather.currentCondition.setDescr(getString("description", JSONWeather));
			weather.currentCondition.setCondition(getString("main", JSONWeather));
			weather.currentCondition.setIcon(getString("icon", JSONWeather));
			
			JSONObject mainObj = getObject("main", jObj);
			weather.currentCondition.setHumidity(getInt("humidity", mainObj));
			weather.currentCondition.setPressure(getInt("pressure", mainObj));
			weather.temperature.setMaxTemp(getFloat("temp_max", mainObj));
			weather.temperature.setMinTemp(getFloat("temp_min", mainObj));
			weather.temperature.setTemp(getFloat("temp", mainObj));
			
			// Wind
			JSONObject wObj = getObject("wind", jObj);
			weather.wind.setSpeed(getFloat("speed", wObj));
			weather.wind.setDeg(getFloat("deg", wObj));
			
			// Clouds
			JSONObject cObj = getObject("clouds", jObj);
			weather.clouds.setPerc(getInt("all", cObj));
			
			// Rain AL added to get rain just in case
			JSONObject rObj = getObject("rain", jObj);
			if (null != rObj) {
				float rainAmount = getFloat("3h", rObj);
				// check for Float.Nan per http://stackoverflow.com/questions/9341653/float-nan-float-nan
				if (rainAmount == rainAmount) {
					weather.rain.setAmmount(getFloat("3h", rObj));
				}
			}
			
			// We download the icon to show
						
			return weather;
		} catch (JSONException e) {
			//Log.i(LOG_TAG, e.getMessage());
			Log.i(LOG_TAG, e.toString());
			return weather;
		}
	}

	private static JSONObject getObject(String tagName, JSONObject jObj) {
		try {
			JSONObject subObj = jObj.getJSONObject(tagName);
			return subObj;
		} catch (JSONException e) {
			Log.i(LOG_TAG, "getObject JSONException with tag - " + tagName);
			return null;
		}
	}
	
	private static String getString(String tagName, JSONObject jObj) {
		try {
			return jObj.getString(tagName);
		} catch (JSONException e) {
			Log.i(LOG_TAG, "getString JSONException with tag - " + tagName);
			return null;
		}
	}

	private static float  getFloat(String tagName, JSONObject jObj) {
		try {
			return (float) jObj.getDouble(tagName);
		} catch (JSONException e) {
			Log.i(LOG_TAG, "getString JSONException with tag - " + tagName);
			return Float.NaN;
		}
	}
	
	private static int  getInt(String tagName, JSONObject jObj) throws JSONException {
		try {
			return jObj.getInt(tagName);
		} catch (JSONException e) {
			Log.i(LOG_TAG, "getString JSONException with tag - " + tagName);
			return Integer.MIN_VALUE;
		}
	}
	
	public static int kelvinToFahrenheit(double k) {
		double f = (k - (double)273.15); // to celsius
		f = (f * 9)/5 + 32;
		return (int) f;
	}

	@Override
	public String getWeatherDataFromLatLong(PlaceRecord place) {
		return new OpenWeatherMapHttpClient().getWeatherData(
				place.getLocation().getLatitude(), place.getLocation().getLongitude());
	}

	@Override
	public WeatherInfo getWeatherInfoFromWeatherData(String data, PlaceRecord place) {
		try {
			OpenWeatherMapWeather w = getWeather(data);
			Log.i(LOG_TAG, "w.temperature.getTemp() - " 
					+ String.valueOf(w.temperature.getTemp()));
			WeatherInfo wi = new WeatherInfo(
					kelvinToFahrenheit(w.temperature.getMinTemp()),
					kelvinToFahrenheit(w.temperature.getMaxTemp()), 
					kelvinToFahrenheit(w.temperature.getTemp()),
					place);
			Log.i(LOG_TAG, "kelvinToFahrenheit(w.temperature.getMaxTemp()) - " 
					+ String.valueOf(kelvinToFahrenheit(w.temperature.getMaxTemp())));
			if (w.rain.getAmmount() > 0) {
				wi.setRainAmount(w.rain.getAmmount());
			}
			if (w.wind.getSpeed() > 0) {
				wi.setWindSpeed(w.wind.getSpeed());
			}
			return wi;
		} catch (Exception e) {
			//Log.i(LOG_TAG, e.getMessage());
			Log.i(LOG_TAG, e.toString());
			return null;
		}
	}

}
