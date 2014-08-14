package com.adl.closetstylist;


public class ItemDataOccasion {

	private ItemData itemData;
	private int score;
	
	public ItemDataOccasion(ItemData item, int score) {
		this.itemData = item;
		this.score = score;
	}

	public ItemData getItemData() {
		return itemData;
	}

	public void setItemData(ItemData itemData) {
		this.itemData = itemData;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
}
