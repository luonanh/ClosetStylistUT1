package com.adl.closetstylist.match.clothes;

import com.adl.closetstylist.UserProfile;
import com.adl.closetstylist.WeatherInfo;
import com.adl.closetstylist.db.ItemDatabaseHelper;

public abstract class ClothesMatchingFactory {

	public abstract ClothesMatching newInstance(ItemDatabaseHelper dbHelper, 
			WeatherInfo wi, UserProfile up, OccasionEnum oe);
}
