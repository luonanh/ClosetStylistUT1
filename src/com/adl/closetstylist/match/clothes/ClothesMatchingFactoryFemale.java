package com.adl.closetstylist.match.clothes;

import com.adl.closetstylist.UserProfile;
import com.adl.closetstylist.WeatherInfo;
import com.adl.closetstylist.db.ItemDatabaseHelper;

public class ClothesMatchingFactoryFemale extends ClothesMatchingFactory {

	@Override
	public ClothesMatching newInstance(ItemDatabaseHelper dbHelper,
			WeatherInfo wi, UserProfile up, OccasionEnum oe) {
		ClothesMatching cm = null;
		ClothesMatchingComponentFactory cmcf = new ClothesMatchingComponentFactoryFemale(dbHelper, wi, up, oe); 
		cm = new ClothesMatchingFemale(cmcf);
		return cm;
	}
}
