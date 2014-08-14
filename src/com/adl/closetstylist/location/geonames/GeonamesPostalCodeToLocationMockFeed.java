package com.adl.closetstylist.location.geonames;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import android.util.Log;

public class GeonamesPostalCodeToLocationMockFeed {
	private final static String LOG_TAG = GeonamesPostalCodeToLocationMockFeed.class.getCanonicalName();

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
     * http://api.geonames.org/postalCodeSearch?postalcode=78758&maxRows=10&username=anhpopeye
		<geonames>
		<totalResultsCount>1</totalResultsCount>
		<code>
		<postalcode>78758</postalcode>
		<name>Austin</name>
		<countryCode>US</countryCode>
		<lat>30.37643</lat>
		<lng>-97.70776</lng>
		<adminCode1>TX</adminCode1>
		<adminName1>Texas</adminName1>
		<adminCode2>453</adminCode2>
		<adminName2>Travis</adminName2>
		<adminCode3/>
		<adminName3/>
		</code>
		</geonames>
     */
    public static String rawText() {
    	return  "<geonames>" +
    			"<totalResultsCount>1</totalResultsCount>" +
    			"<code>" +
    			"<postalcode>78758</postalcode>" +
    			"<name>Austin</name>" +
    			"<countryCode>US</countryCode>" +
    			"<lat>30.37643</lat>" +
    			"<lng>-97.70776</lng>" +
    			"<adminCode1>TX</adminCode1>" +
    			"<adminName1>Texas</adminName1>" +
    			"<adminCode2>453</adminCode2>" +
    			"<adminName2>Travis</adminName2>" +
    			"<adminCode3/>" +
    			"<adminName3/>" +
    			"</code>" +
    			"</geonames>";
    }
}
