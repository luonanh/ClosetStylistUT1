package com.adl.closetstylist;

import java.util.ArrayList;
import java.util.Arrays;

public enum ItemColorEnum {

	Beige, Black, Blue, Brown, Gray, Green, Orange, Pink, Red, Violet, White, Yellow, Multicolor_Pattern;
	
	public static ArrayList<String> getAllItemColorEnumString() {
		ArrayList<ItemColorEnum> enumList = new ArrayList<ItemColorEnum>(Arrays.asList(ItemColorEnum.values()));
		ArrayList<String> result = new ArrayList<String>();
		for (ItemColorEnum enumType: enumList) {
			result.add(enumType.toString());
		}
		
		//add description
		result.add("-color-");
				
		return result;
	}
}
