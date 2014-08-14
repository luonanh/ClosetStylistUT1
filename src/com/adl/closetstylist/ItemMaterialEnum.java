package com.adl.closetstylist;

import java.util.ArrayList;
import java.util.Arrays;

public enum ItemMaterialEnum {
	
	Cotton_Or_Cotton_Blend, Denim, Down, Jersey_Knit, Leather, Linen, Nylon, Performance, Polyester, Silk, Spandex, Wool_Or_Wool_Blend;
	
	public static ArrayList<String> getAllItemMaterialEnumString() {
		ArrayList<ItemMaterialEnum> enumList = new ArrayList<ItemMaterialEnum>(Arrays.asList(ItemMaterialEnum.values()));
		ArrayList<String> result = new ArrayList<String>();
		for (ItemMaterialEnum enumType: enumList) {
			result.add(enumType.toString());
		}
		
		//add description
		result.add("-material-");
		
		return result;
	}
}
