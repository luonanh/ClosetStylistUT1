package com.adl.closetstylist.location.geonames;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import android.util.Log;

public class GeonamesLocationToPostalCodeMockFeed {
	private final static String LOG_TAG = GeonamesLocationToPostalCodeMockFeed.class.getCanonicalName();

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
     * 
		<geonames>
		<code>
		<postalcode>78717</postalcode>
		<name>Austin</name>
		<countryCode>US</countryCode>
		<lat>30.4884</lat>
		<lng>-97.71751</lng>
		<adminCode1>TX</adminCode1>
		<adminName1>Texas</adminName1>
		<adminCode2>491</adminCode2>
		<adminName2>Williamson</adminName2>
		<adminCode3/>
		<adminName3/>
		<distance>0</distance>
		</code>
		<code>
		<postalcode>78681</postalcode>
		<name>Round Rock</name>
		<countryCode>US</countryCode>
		<lat>30.50843</lat>
		<lng>-97.70617</lng>
		<adminCode1>TX</adminCode1>
		<adminName1>Texas</adminName1>
		<adminCode2>491</adminCode2>
		<adminName2>Williamson</adminName2>
		<adminCode3/>
		<adminName3/>
		<distance>2.47815</distance>
		</code>
		<code>
		<postalcode>78664</postalcode>
		<name>Round Rock</name>
		<countryCode>US</countryCode>
		<lat>30.51452</lat>
		<lng>-97.66803</lng>
		<adminCode1>TX</adminCode1>
		<adminName1>Texas</adminName1>
		<adminCode2>491</adminCode2>
		<adminName2>Williamson</adminName2>
		<adminCode3/>
		<adminName3/>
		<distance>5.55958</distance>
		</code>
		<code>
		<postalcode>78665</postalcode>
		<name>Round Rock</name>
		<countryCode>US</countryCode>
		<lat>30.51452</lat>
		<lng>-97.66803</lng>
		<adminCode1>TX</adminCode1>
		<adminName1>Texas</adminName1>
		<adminCode2>491</adminCode2>
		<adminName2>Williamson</adminName2>
		<adminCode3/>
		<adminName3/>
		<distance>5.55958</distance>
		</code>
		<code>
		<postalcode>78728</postalcode>
		<name>Austin</name>
		<countryCode>US</countryCode>
		<lat>30.44168</lat>
		<lng>-97.68112</lng>
		<adminCode1>TX</adminCode1>
		<adminName1>Texas</adminName1>
		<adminCode2>453</adminCode2>
		<adminName2>Travis</adminName2>
		<adminCode3/>
		<adminName3/>
		<distance>6.2569</distance>
		</code>
		</geonames>
     */
    public static String rawText() {
    	// http://api.geonames.org/findNearbyPostalCodes?lat=30.4883997&lng=-97.7175117&username=demo
    	return 
    			"<geonames>" +
    			"<code>" +
    			"<postalcode>78717</postalcode>" +
    			"<name>Austin</name>" +
    			"<countryCode>US</countryCode>" +
    			"<lat>30.4884</lat>" +
    			"<lng>-97.71751</lng>" +
    			"<adminCode1>TX</adminCode1>" +
    			"<adminName1>Texas</adminName1>" +
    			"<adminCode2>491</adminCode2>" +
    			"<adminName2>Williamson</adminName2>" +
    			"<adminCode3/>" +
    			"<adminName3/>" +
    			"<distance>0</distance>" +
    			"</code>" +
    			"<code>" +
    			"<postalcode>78681</postalcode>" +
    			"<name>Round Rock</name>" +
    			"<countryCode>US</countryCode>" +
    			"<lat>30.50843</lat>" +
    			"<lng>-97.70617</lng>" +
    			"<adminCode1>TX</adminCode1>" +
    			"<adminName1>Texas</adminName1>" +
    			"<adminCode2>491</adminCode2>" +
    			"<adminName2>Williamson</adminName2>" +
    			"<adminCode3/>" +
    			"<adminName3/>" +
    			"<distance>2.47815</distance>" +
    			"</code>" +
    			"<code>" +
    			"<postalcode>78664</postalcode>" +
    			"<name>Round Rock</name>" +
    			"<countryCode>US</countryCode>" +
    			"<lat>30.51452</lat>" +
    			"<lng>-97.66803</lng>" +
    			"<adminCode1>TX</adminCode1>" +
    			"<adminName1>Texas</adminName1>" +
    			"<adminCode2>491</adminCode2>" +
    			"<adminName2>Williamson</adminName2>" +
    			"<adminCode3/>" +
    			"<adminName3/>" +
    			"<distance>5.55958</distance>" +
    			"</code>" +
    			"<code>" +
    			"<postalcode>78665</postalcode>" +
    			"<name>Round Rock</name>" +
    			"<countryCode>US</countryCode>" +
    			"<lat>30.51452</lat>" +
    			"<lng>-97.66803</lng>" +
    			"<adminCode1>TX</adminCode1>" +
    			"<adminName1>Texas</adminName1>" +
    			"<adminCode2>491</adminCode2>" +
    			"<adminName2>Williamson</adminName2>" +
    			"<adminCode3/>" +
    			"<adminName3/>" +
    			"<distance>5.55958</distance>" +
    			"</code>" +
    			"<code>" +
    			"<postalcode>78728</postalcode>" +
    			"<name>Austin</name>" +
    			"<countryCode>US</countryCode>" +
    			"<lat>30.44168</lat>" +
    			"<lng>-97.68112</lng>" +
    			"<adminCode1>TX</adminCode1>" +
    			"<adminName1>Texas</adminName1>" +
    			"<adminCode2>453</adminCode2>" +
    			"<adminName2>Travis</adminName2>" +
    			"<adminCode3/>" +
    			"<adminName3/>" +
    			"<distance>6.2569</distance>" +
    			"</code>" +
    			"</geonames>";
    }
}
