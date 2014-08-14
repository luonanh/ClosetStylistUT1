package com.adl.closetstylist.match.clothes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.adl.closetstylist.ItemCategoryEnum;
import com.adl.closetstylist.ItemDataOccasion;
import com.adl.closetstylist.ItemStyleEnum;
import com.adl.closetstylist.UserProfile;
import com.adl.closetstylist.WeatherInfo;
import com.adl.closetstylist.db.ItemDatabaseHelper;

public class PairMatchingFemale extends PairMatching {
	private ArrayList<ItemStyleEnum> topStyle = new ArrayList<ItemStyleEnum>(Arrays.asList(
			ItemStyleEnum.Collared_And_Button_Down,
			ItemStyleEnum.Blouse_Short_Sleeve,
			ItemStyleEnum.Blouse_Long_Sleeve,
			ItemStyleEnum.Blouse_Sleeveless,
			ItemStyleEnum.T_Shirt_Long_Sleeve,
			ItemStyleEnum.T_Shirt_Short_Sleeve,
			ItemStyleEnum.Tank_Camisoles,
			ItemStyleEnum.Party_Top,
			ItemStyleEnum.Tunic,
			ItemStyleEnum.Pull_Over));

	private ArrayList<ItemStyleEnum> outerStyle = new ArrayList<ItemStyleEnum>(); 

	public PairMatchingFemale(ItemDatabaseHelper dbHelper,
			WeatherInfo wi, UserProfile up, OccasionEnum oe) {
		this.dbHelper = dbHelper;
		this.wi = wi;
		this.pairMatchingRecordTable = this.dbHelper.getPairMatchingRecordFemale();
		setupPerWeatherInfo();
	}
	
	/*
	@Override
	protected Outfit getOutfitFromTopBottomOuter(ItemDataOccasion top, ItemDataOccasion bottom,
			ItemDataOccasion outer) {
		Outfit outfit = null;
		String topStyle = top.getItemData().getStyle();
		String bottomStyle = bottom.getItemData().getStyle();
		if (null == outer) {
			for (PairMatchingRecord pmr: pairMatchingRecordTable) {
				if ((pmr.getTop().equalsIgnoreCase(topStyle))
						&& (pmr.getBottom().equalsIgnoreCase(bottomStyle))) {
					int totalScore = top.getScore() + bottom.getScore() + pmr.getPoint();
					outfit = new Outfit.OutfitBuilder(top.getItemData())
							.bottom(bottom.getItemData())
							.score(totalScore)
							.build();
				}
			}
		} else {
			for (PairMatchingRecord pmr: pairMatchingRecordTable) {
				if ((pmr.getTop().equalsIgnoreCase(topStyle))
						&& (pmr.getBottom().equalsIgnoreCase(bottomStyle))) {
					int totalScore = 0;
					if (pmr.getOuter().equalsIgnoreCase("No")) {
						totalScore = top.getScore() + bottom.getScore() + pmr.getPoint();						
					} else {
						totalScore = top.getScore() + bottom.getScore() 
								+ outer.getScore() + pmr.getPoint();
					}
					outfit = new Outfit.OutfitBuilder(top.getItemData())
							.bottom(bottom.getItemData())
							.outer(outer.getItemData())
							.score(totalScore)
							.build(); 
				}
			}			
		}
		return outfit;
	}
	*/

	@Override
	protected List<ItemDataOccasion> getOuterList(List<ItemDataOccasion> top) {
		ArrayList<ItemDataOccasion> result = new ArrayList<ItemDataOccasion>();
		for (ItemDataOccasion ido: top) {
			if ((ido.getItemData().getCategory() == ItemCategoryEnum.Top)
					&& (isOuter(ido.getItemData().getStyle()))) {
				result.add(ido);
			}
		}
		return result;
	}

	@Override
	protected List<ItemDataOccasion> getTopList(List<ItemDataOccasion> top) {
		ArrayList<ItemDataOccasion> result = new ArrayList<ItemDataOccasion>();
		for (ItemDataOccasion ido: top) {
			if ((ido.getItemData().getCategory() == ItemCategoryEnum.Top)
					&& (isTop(ido.getItemData().getStyle()))) {
				result.add(ido);
			}
		}
		return result;
	}
	
	private boolean isOuter(ItemStyleEnum style) {
		for (ItemStyleEnum temp: outerStyle) {
			if (temp == style) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isTop(ItemStyleEnum style) {
		for (ItemStyleEnum temp: topStyle) {
			if (temp == style) {
				return true;
			}
		}
		return false;
	}
	
	/*
	 * This function sets up outerStyle and pairMatchingRecordTable based on 
	 * WeatherInfo as followings:
	 * 	- tempMax > 70: no need Outer -> Outer set to "No" for all entries
	 * 	- 70 > tempMax > 40: use default Outer value, outerStyle includes some
	 * 	- tempMax < 40: use default Outer value, outerStyle includes only one
	 * 
	 */
	private void setupPerWeatherInfo() {
		if (wi.getTempMax() > 70) {
			for (PairMatchingRecord pmr: pairMatchingRecordTable) {
				pmr.setOuter(ItemStyleEnum.No);
			}
		} else if (wi.getTempMax() > 40) {
			outerStyle.add(ItemStyleEnum.Cardigan);
			outerStyle.add(ItemStyleEnum.Sweater_And_Sweatshirt);
			outerStyle.add(ItemStyleEnum.Vest);
			outerStyle.add(ItemStyleEnum.Coat_And_Jacket_Light);
		} else {
			outerStyle.add(ItemStyleEnum.Coat_And_Jacket_Heavy);
		}
	}
}
