package com.adl.closetstylist.match.clothes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public enum OccasionEnum {

	Formal(10000001),
	Semi_Formal(10000002),
	Casual(10000003),
	Day_Out(10000004),
	Night_Out(10000005);
	
	private int id;
	
	private OccasionEnum(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public static OccasionEnum getById(int id) {
		for (OccasionEnum occasion : values())
			if (occasion.getId() == id)
				return occasion;
		
		return null;
	}
	
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
