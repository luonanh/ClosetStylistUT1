package com.adl.closetstylist.match.clothes;


public interface ClothesMatchingComponentFactory {

	/*
	public OccasionMatching createOccasionMatching(ItemDatabaseHelper dbHelper, OccasionEnum oe);
	public PairMatching createPairMatching(ItemDatabaseHelper dbHelper, WeatherInfo wi);
	public ColorMatching createColorMatching(ItemDatabaseHelper dbHelper);
	*/
	public OccasionMatching newOccasionMatching();
	public PairMatching newPairMatching();
	public ColorMatching newColorMatching();
}
