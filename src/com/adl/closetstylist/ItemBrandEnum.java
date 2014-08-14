package com.adl.closetstylist;

import java.util.ArrayList;
import java.util.Arrays;

public enum ItemBrandEnum {

	Banana, Express, RalphLauren, CK, Adidas, Nike, Guess, Oakley, DKNY, FrenchConnection, JCrew, AE, AF, LuckyBrands, SevenJeans, Rei, Dockers, Aeropostale, KennethCole, Diesel, GordonCooper, Arizona;
	
	public static ArrayList<String> getAllItemBrandEnumString() {
		ArrayList<ItemBrandEnum> enumList = new ArrayList<ItemBrandEnum>(Arrays.asList(ItemBrandEnum.values()));
		ArrayList<String> result = new ArrayList<String>();
		for (ItemBrandEnum enumType: enumList) {
			result.add(enumType.toString());
		}
		return result;
	}
}
