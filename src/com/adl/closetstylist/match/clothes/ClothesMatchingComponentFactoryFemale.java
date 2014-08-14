package com.adl.closetstylist.match.clothes;

import com.adl.closetstylist.UserProfile;
import com.adl.closetstylist.WeatherInfo;
import com.adl.closetstylist.db.ItemDatabaseHelper;

public class ClothesMatchingComponentFactoryFemale implements ClothesMatchingComponentFactory {
	private ItemDatabaseHelper dbHelper;
	private WeatherInfo wi;
	private UserProfile up;
	private OccasionEnum oe;

	public ClothesMatchingComponentFactoryFemale(ItemDatabaseHelper dbHelper,
			WeatherInfo wi, UserProfile up, OccasionEnum oe) {
		this.dbHelper = dbHelper;
		this.wi = wi;
		this.up = up;
		this.oe = oe;
	}
	
	@Override
	public OccasionMatching newOccasionMatching() {
		return new OccasionMatchingFemale(dbHelper, wi, up, oe);
	}

	@Override
	public PairMatching newPairMatching() {
		return new PairMatchingFemale(dbHelper, wi, up, oe);
	}

	@Override
	public ColorMatching newColorMatching() {
		return new ColorMatchingDefault(dbHelper, wi, up, oe);
	}
}
