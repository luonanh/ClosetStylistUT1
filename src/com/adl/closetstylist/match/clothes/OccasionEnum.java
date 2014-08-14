package com.adl.closetstylist.match.clothes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum OccasionEnum {

	Formal,
	Semi_Formal,
	Casual,
	Day_Out,
	Night_Out;
	
	public List<OccasionEnum> getAllOccasionEnum() {
		return new ArrayList<OccasionEnum>(Arrays.asList(OccasionEnum.values()));
	}
	
	/*
	 * This can be used to populate the Spinner in OutfitOfTheDay activity. 
	 */
	public static ArrayList<String> getAllOccasionEnumString() {
		ArrayList<OccasionEnum> enumList = new ArrayList<OccasionEnum>(Arrays.asList(OccasionEnum.values()));
		ArrayList<String> result = new ArrayList<String>();
		for (OccasionEnum enumType: enumList) {
			result.add(enumType.toString());
		}
		return result;
	}
}
