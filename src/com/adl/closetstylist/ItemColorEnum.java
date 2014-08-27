package com.adl.closetstylist;

import java.util.ArrayList;
import java.util.Arrays;

public enum ItemColorEnum {

	Beige(R.drawable.beige),
	Black(R.drawable.black),
	Blue(R.drawable.blue),
	Brown(R.drawable.brown),
	Gray(R.drawable.gray),
	Green(R.drawable.green),
	Orange(R.drawable.orange),
	Pink(R.drawable.pink), 
	Red(R.drawable.red),
	Violet(R.drawable.violet),
	White(R.drawable.white),
	Yellow(R.drawable.yello),
	Multicolor_Pattern;
	
	private int colorImageResourceId;

	private ItemColorEnum() {
		
	}
	
	private ItemColorEnum(int colorImageResourceId) {
		this.colorImageResourceId = colorImageResourceId;
	}
	
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

	public int getColorImageResourceId() {
		return colorImageResourceId;
	}

}
