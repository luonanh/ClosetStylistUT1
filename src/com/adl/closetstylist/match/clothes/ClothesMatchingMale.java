package com.adl.closetstylist.match.clothes;


public class ClothesMatchingMale extends ClothesMatching {

	/*
	public ClothesMatchingMale(ClothesMatchingComponentFactory cmcf, 
			ItemDatabaseHelper dbHelper, WeatherInfo wi, UserProfile up, OccasionEnum oe) {*/
	public ClothesMatchingMale(ClothesMatchingComponentFactory cmcf) {		
		this.cmcf = cmcf;
		this.om = cmcf.newOccasionMatching();
		this.pm = cmcf.newPairMatching();
		this.cm = cmcf.newColorMatching();
	}
}
