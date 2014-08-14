package com.adl.closetstylist.match.clothes;

import com.adl.closetstylist.UserProfile;
import com.adl.closetstylist.WeatherInfo;
import com.adl.closetstylist.db.ItemDatabaseHelper;

public class OccasionMatchingMale extends OccasionMatching {
	public OccasionMatchingMale(ItemDatabaseHelper dbHelper,
			WeatherInfo wi, UserProfile up, OccasionEnum oe) {
		this.dbHelper = dbHelper;
		this.oe = oe;
		this.occasionMatchingRecordTable = this.dbHelper
				.getOccasionMatchingRecordMale(this.oe);
	}
}