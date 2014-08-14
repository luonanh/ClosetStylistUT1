package com.adl.closetstylist;

import java.util.ArrayList;
import java.util.Arrays;

public enum DayEnum {
	SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY;
	
	public static ArrayList<String> getAllDayEnumString() {
		ArrayList<DayEnum> enumList = new ArrayList<DayEnum>(Arrays.asList(DayEnum.values()));
		ArrayList<String> result = new ArrayList<String>();
		for (DayEnum enumType: enumList) {
			result.add(enumType.toString());
		}
		return result;
	}
}
