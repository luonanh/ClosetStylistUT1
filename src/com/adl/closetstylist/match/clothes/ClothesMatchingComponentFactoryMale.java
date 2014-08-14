package com.adl.closetstylist.match.clothes;

import com.adl.closetstylist.UserProfile;
import com.adl.closetstylist.WeatherInfo;
import com.adl.closetstylist.db.ItemDatabaseHelper;

public class ClothesMatchingComponentFactoryMale implements ClothesMatchingComponentFactory {
	private ItemDatabaseHelper dbHelper;
	private WeatherInfo wi;
	private UserProfile up;
	private OccasionEnum oe;

	public ClothesMatchingComponentFactoryMale(ItemDatabaseHelper dbHelper,
			WeatherInfo wi, UserProfile up, OccasionEnum oe) {
		this.dbHelper = dbHelper;
		this.wi = wi;
		this.up = up;
		this.oe = oe;
	}
	
	@Override
	public OccasionMatching newOccasionMatching() {
		return new OccasionMatchingMale(dbHelper, wi, up, oe);
	}

	@Override
	public PairMatching newPairMatching() {
		return new PairMatchingMale(dbHelper, wi, up, oe);
	}

	@Override
	public ColorMatching newColorMatching() {
		return new ColorMatchingDefault(dbHelper, wi, up, oe);
	}

}
