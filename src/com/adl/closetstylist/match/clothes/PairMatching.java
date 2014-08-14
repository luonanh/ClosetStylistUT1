package com.adl.closetstylist.match.clothes;

import java.util.ArrayList;
import java.util.List;

import com.adl.closetstylist.ItemDataOccasion;
import com.adl.closetstylist.ItemStyleEnum;
import com.adl.closetstylist.Outfit;
import com.adl.closetstylist.WeatherInfo;
import com.adl.closetstylist.Outfit.OutfitBuilder;
import com.adl.closetstylist.db.ItemDatabaseHelper;

public abstract class PairMatching {

	// All of the below instance variables must be initialize in constructor. 
	// Otherwise all of the functions will fail.
	protected ArrayList<PairMatchingRecord> pairMatchingRecordTable;
	protected ItemDatabaseHelper dbHelper;
	protected WeatherInfo wi;
	
	// The following instance variables need not to be initialized in constructor.
	private List<ItemDataOccasion> topPairList;		// created by setInternalList
	private List<ItemDataOccasion> bottomPairList;	// created by setInternalList
	private List<ItemDataOccasion> outerPairList;	// created by setInternalList
	private ArrayList<Outfit> pairMatchingList;		// created by getPairScoreList
	
	// calculate points for a particular combination of top, bottom, outer 
	// (optional) and return a new Outfit.
	// null is returned if nothing is found
	protected Outfit getOutfitFromTopBottomOuter(ItemDataOccasion top, 
			ItemDataOccasion bottom, ItemDataOccasion outer) {
		Outfit outfit = null;
		ItemStyleEnum topStyle = top.getItemData().getStyle();
		ItemStyleEnum bottomStyle = bottom.getItemData().getStyle();
		if (null == outer) {
			for (PairMatchingRecord pmr: pairMatchingRecordTable) {
				if ((pmr.getTop() == topStyle)
						&& (pmr.getBottom() == bottomStyle)) {
					int totalScore = top.getScore() + bottom.getScore() + pmr.getPoint();
					outfit = new Outfit.OutfitBuilder(top.getItemData())
							.bottom(bottom.getItemData())
							.score(totalScore)
							.build();
				}
			}
		} else {
			for (PairMatchingRecord pmr: pairMatchingRecordTable) {
				if ((pmr.getTop() == topStyle)
						&& (pmr.getBottom() == bottomStyle)) {
					int totalScore = 0;
					if (pmr.getOuter() == ItemStyleEnum.No) {
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
	
	protected abstract List<ItemDataOccasion> getOuterList(List<ItemDataOccasion> top);
	
	protected abstract List<ItemDataOccasion> getTopList(List<ItemDataOccasion> top);
		
	public List<Outfit> getPairScoreList(List<ItemDataOccasion> topList, 
			List<ItemDataOccasion> bottomList) {
		if (null != pairMatchingList) {
			pairMatchingList.clear();
		} else {
			pairMatchingList = new ArrayList<Outfit>();
		}
		
		setInternalLists(topList, bottomList);
		
		for (ItemDataOccasion top: topPairList) {
			for (ItemDataOccasion bottom: bottomPairList) {
				Outfit o = null;
				
				// For outfit with add-on
				for (ItemDataOccasion outer: outerPairList) {
					o = getOutfitFromTopBottomOuter(top, bottom, outer);
					if (null != o) {
						if (!pairMatchingList.contains(o)) {
							pairMatchingList.add(o);
						}
					}
				}
				
				// For outfit with no add-on
				o = getOutfitFromTopBottomOuter(top, bottom, null);
				if (null != o) {
					if (!pairMatchingList.contains(o)) {
						pairMatchingList.add(o);
					}
				}	
			}
		}
		
		/*
		 * The above for loops will create many duplicated element in the List
		 * due to some combinations of top and bottom do not need outer.
		 */
		/*
		for (int i = 0; i < pairMatchingList.size(); i++) {
			Outfit o1 = pairMatchingList.get(i);
			ItemData top1 = o1.getTop();
			ItemData bottom1 = o1.getBottom();
			ItemData outer1 = o1.getOuter();
			int score1 = o1.getScore();
			for (int j = i; j < pairMatchingList.size(); j++) {
				Outfit o2 = pairMatchingList.get(j);
				ItemData top2 = o2.getTop();
				ItemData bottom2 = o2.getBottom();
				ItemData outer2 = o2.getOuter();
				int score2 = o2.getScore();
				if (score1 == score2) {
					pairMatchingList.remove(j);
				}
			}
		}
		*/
		
		return pairMatchingList;
	}
	
	private void setInternalLists(List<ItemDataOccasion> topList, 
			List<ItemDataOccasion> bottomList) {
		topPairList = getTopList(topList);
		outerPairList = getOuterList(topList);
		bottomPairList = bottomList;
	}
}
