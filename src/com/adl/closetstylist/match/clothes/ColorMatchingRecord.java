package com.adl.closetstylist.match.clothes;

import com.adl.closetstylist.ItemColorEnum;

public class ColorMatchingRecord {

	private long id;
	private ItemColorEnum bottom;
	private ItemColorEnum top;
	private int point;
	
	public ColorMatchingRecord(ItemColorEnum bottom, ItemColorEnum top, int point) {
		this.bottom = bottom;
		this.top = top;
		this.point = point;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public ItemColorEnum getBottom() {
		return bottom;
	}

	public void setBottom(ItemColorEnum bottom) {
		this.bottom = bottom;
	}

	public ItemColorEnum getTop() {
		return top;
	}

	public void setTop(ItemColorEnum top) {
		this.top = top;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}
}
