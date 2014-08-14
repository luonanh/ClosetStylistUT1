package com.adl.closetstylist.match.clothes;

import com.adl.closetstylist.ItemStyleEnum;

public class PairMatchingRecord {

	private long id;
	private ItemStyleEnum bottom;
	private ItemStyleEnum top;
	private int point;
	private ItemStyleEnum outer;
	
	public PairMatchingRecord(ItemStyleEnum bottom, ItemStyleEnum top, int point, ItemStyleEnum outer) {
		this.bottom = bottom;
		this.top = top;
		this.point = point;
		this.outer = outer;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public ItemStyleEnum getBottom() {
		return bottom;
	}

	public void setBottom(ItemStyleEnum bottom) {
		this.bottom = bottom;
	}

	public ItemStyleEnum getTop() {
		return top;
	}

	public void setTop(ItemStyleEnum top) {
		this.top = top;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public ItemStyleEnum getOuter() {
		return outer;
	}

	public void setOuter(ItemStyleEnum outer) {
		this.outer = outer;
	}
}
