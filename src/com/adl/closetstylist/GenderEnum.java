package com.adl.closetstylist;

import java.util.ArrayList;
import java.util.Arrays;

public enum GenderEnum {
	MALE, FEMALE;
	
	public static ArrayList<String> getAllGenderString() {
		ArrayList<GenderEnum> enumList = new ArrayList<GenderEnum>(Arrays.asList(GenderEnum.values()));
		ArrayList<String> result = new ArrayList<String>();
		for (GenderEnum enumType: enumList) {
			result.add(enumType.toString());
		}
		return result;
	}
}
