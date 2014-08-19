package com.adl.closetstylist.weather;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import android.util.Log;

public class OpenWeatherMapMockFeed {
	private final static String LOG_TAG = OpenWeatherMapMockFeed.class.getCanonicalName();

	public static final String ENCODING = "US-ASCII";

    public static InputStream rawStream() {
        try {
            byte[] bytes = rawText().getBytes(ENCODING);
            return new ByteArrayInputStream(bytes);
        } catch (Exception e) {
        	Log.i(LOG_TAG, e.getMessage());
            return null;
        }
    }

    /*
     * http://api.openweathermap.org/data/2.5/weather?lat=30.4883997&lon=-97.7175117
     */
    public static String rawText() {
    	//return "{\"coord\":{\"lon\":-97.72,\"lat\":30.49},\"sys\":{\"message\":0.4248,\"country\":\"US\",\"sunrise\":1403263741,\"sunset\":1403314572},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"sky is clear\",\"icon\":\"01n\"}],\"base\":\"cmc stations\",\"main\":{\"temp\":300.87,\"pressure\":1017,\"humidity\":58,\"temp_min\":299.82,\"temp_max\":302.04},\"wind\":{\"speed\":3.1,\"deg\":140},\"clouds\":{\"all\":1},\"dt\":1403232774,\"id\":4724129,\"name\":\"Round Rock\",\"cod\":200}";
    	//return "{\"coord\":{\"lon\":-97.72,\"lat\":30.49},\"sys\":{\"message\":0.4248,\"country\":\"US\",\"sunrise\":1403263741,\"sunset\":1403314572},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"sky is clear\",\"icon\":\"01n\"}],\"base\":\"cmc stations\",\"main\":{\"temp\":300.87,\"pressure\":1017,\"humidity\":58,\"temp_min\":299.82,\"temp_max\":302.04},\"wind\":{\"speed\":3.1,\"deg\":140},\"clouds\":{\"all\":90}, \"rain\":{\"3h\":3},\"dt\":1403232774,\"id\":4724129,\"name\":\"Round Rock\",\"cod\":200}";
    	
    	// cold weather - current 50, min 30, max 55
    	//return "{\"coord\":{\"lon\":-97.72,\"lat\":30.49},\"sys\":{\"message\":0.4248,\"country\":\"US\",\"sunrise\":1403263741,\"sunset\":1403314572},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"sky is clear\",\"icon\":\"01n\"}],\"base\":\"cmc stations\",\"main\":{\"temp\":283.15,\"pressure\":1017,\"humidity\":58,\"temp_min\":272.04,\"temp_max\":285.93},\"wind\":{\"speed\":3.1,\"deg\":140},\"clouds\":{\"all\":90}, \"rain\":{\"3h\":3},\"dt\":1403232774,\"id\":4724129,\"name\":\"Round Rock\",\"cod\":200}";
    	
    	// hot weather - current 95.8, min 95, max 96.8
    	return "{\"coord\":{\"lon\":-97.72,\"lat\":30.49},\"sys\":{\"type\":1,\"id\":2558,\"message\":0.4358,\"country\":\"US\",\"sunrise\":1408363188,\"sunset\":1408410559},\"weather\":[{\"id\":801,\"main\":\"Clouds\",\"description\":\"few clouds\",\"icon\":\"02d\"}],\"base\":\"cmc stations\",\"main\":{\"temp\":308.63,\"pressure\":1013,\"humidity\":37,\"temp_min\":308.15,\"temp_max\":309.15},\"wind\":{\"speed\":3.1,\"deg\":160},\"clouds\":{\"all\":20},\"dt\":1408391460,\"id\":4724129,\"name\":\"Round Rock\",\"cod\":200}";
    }
}
