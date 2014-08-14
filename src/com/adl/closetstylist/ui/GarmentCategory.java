package com.adl.closetstylist.ui;

import com.adl.closetstylist.R;

public enum GarmentCategory {
	JACKET(1, R.string.mycloset_jacket),
	TOP(2, R.string.mycloset_top),
	BOTTOM(3, R.string.mycloset_bottom);
	
	private int id;
	private int labelResourceId;

	private GarmentCategory(int id, int labelResourceId) {
		this.id = id;
		this.labelResourceId = labelResourceId;
	}
	
	public static GarmentCategory getById(int id) {
		switch (id) {
		case 1: return JACKET;
		case 2: return TOP;
		case 3: return BOTTOM;
		
		default:
			return null;
		}
	}
	
	public int getId(){
		return id;
	}

	public int getLabelResourceId() {
		return labelResourceId;
	}
	
	
}
