package com.adl.closetstylist.location.geonames;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.adl.closetstylist.PlaceRecord;

import android.location.Location;
import android.location.LocationManager;

public class LocationInfoProvider {
	private final static String LOG_TAG = LocationInfoProvider.class.getCanonicalName();
	
	// Change to false if you don't have network access
	private static final boolean HAS_NETWORK = false;

	// put your www.geonames.org account name here.
	private static String USERNAME = "anhpopeye";
	
	public static PlaceRecord getPlaceFromPostalCode(Integer... zip) {
		return getPlaceFromURL(
				generateURLSearchLocationFromPostalCode(USERNAME, zip[0]),
				GeonamesPostalCodeToLocationMockFeed.rawText());
	}
	
	public static PlaceRecord getPlaceFromPostalCode(Location... loc) {
		return getPlaceFromURL(
				generateURLSearchPostalCodeFromLocation(USERNAME,
						loc[0].getLatitude(), loc[0].getLongitude()), 
				GeonamesLocationToPostalCodeMockFeed.rawText());
	}
	
	/*
	 * The first parameter is the generated URL
	 * The second parameter is the mockup feed
	 */
	private static PlaceRecord getPlaceFromURL(String... params) {
		HttpURLConnection mHttpUrl = null;
		String result = null;
		BufferedReader in = null;

		if (HAS_NETWORK) {
			try {

				URL url = new URL(params[0]);
				mHttpUrl = (HttpURLConnection) url.openConnection();
				in = new BufferedReader(new InputStreamReader(
						mHttpUrl.getInputStream()));

				StringBuffer sb = new StringBuffer("");
				String line = "";
				while ((line = in.readLine()) != null) {
					sb.append(line + "\n");
				}
				result = sb.toString();

			} catch (MalformedURLException e) {

			} catch (IOException e) {

			} finally {
				try {
					if (null != in) {
						in.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				mHttpUrl.disconnect();
			}			
		} else {
			result = params[1];
		}

		return placeDataFromXml(result);
	}

	private static PlaceRecord placeDataFromXml(String xmlString) {
		DocumentBuilder builder;
		String stateName = "";
		String placeName = "";
		String countryCode = "";
		int postalCode = 0;
		double lng;
		double lat;
		Location tempLocation = new Location(LocationManager.NETWORK_PROVIDER);

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		try {
			builder = factory.newDocumentBuilder();
			Document document = builder.parse(new InputSource(new StringReader(
					xmlString)));
			NodeList list = document.getDocumentElement().getChildNodes();
			for (int i = 0; i < list.getLength(); i++) {
				Node curr = list.item(i);

				NodeList list2 = curr.getChildNodes();

				for (int j = 0; j < list2.getLength(); j++) {

					Node curr2 = list2.item(j);
					if (curr2.getNodeName() != null) {

						if (curr2.getNodeName().equals("postalcode")) {
							postalCode = Integer.parseInt(curr2.getTextContent());
						} else if (curr2.getNodeName().equals("name")) {
							placeName = curr2.getTextContent();
						} else if (curr2.getNodeName().equals("countryCode")) {
							countryCode = curr2.getTextContent();
						} else if (curr2.getNodeName().equals("lat")) {
							lat = Double.parseDouble(curr2.getTextContent());
							tempLocation.setLatitude(lat);;
						} else if (curr2.getNodeName().equals("lng")) {
							lng = Double.parseDouble(curr2.getTextContent());
							tempLocation.setLongitude(lng);
						} else if (curr2.getNodeName().equals("adminName1")) {
							stateName = curr2.getTextContent();
						}
					}
				}
			}
		} catch (DOMException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new PlaceRecord(countryCode, stateName, placeName, postalCode, tempLocation);
	}
	
	private static String generateURLSearchPostalCodeFromLocation(String username, double lat, double lng) {
		// http://api.geonames.org/findNearbyPostalCodes?lat=30.4883997&lng=-97.7175117&username=anhpopeye
		return "http://api.geonames.org/findNearbyPostalCodes?lat=" + String.valueOf(lat)
				+ "&lng=" + String.valueOf(lng) +  "&username=" + username;
	}
	
	private static String generateURLSearchLocationFromPostalCode(String username, int zip) {
		// http://api.geonames.org/postalCodeSearch?postalcode=78758&maxRows=10&username=anhpopeye
		return "http://api.geonames.org/postalCodeSearch?postalcode=" + zip 
				+ "&maxRows=2" + "&username=" + username;
	}

}
