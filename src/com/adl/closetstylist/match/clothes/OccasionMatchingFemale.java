package com.adl.closetstylist.match.clothes;

import com.adl.closetstylist.UserProfile;
import com.adl.closetstylist.WeatherInfo;
import com.adl.closetstylist.db.ItemDatabaseHelper;

public class OccasionMatchingFemale extends OccasionMatching {
	public OccasionMatchingFemale(ItemDatabaseHelper dbHelper,
			WeatherInfo wi, UserProfile up, OccasionEnum oe) {
		this.dbHelper = dbHelper;
		this.oe = oe;
		this.occasionMatchingRecordTable = this.dbHelper
				.getOccasionMatchingRecordFemale(this.oe);
	}
}
