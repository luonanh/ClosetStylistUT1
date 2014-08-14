package com.adl.closetstylist.match.clothes;


public class OccasionMatchingRecord {

	private long id;
	private String category;
	private String style;
	private int formal;
	private int semiFormal;
	private int casual;
	private int dayOut;
	private int nightOut;
	
	public OccasionMatchingRecord(String category, String style, int formal, 
			int semiFormal, int casual, int dayOut, int nightOut) {
		this.category = category;
		this.style = style;
		this.formal = formal;
		this.semiFormal = semiFormal;
		this.casual = casual;
		this.dayOut = dayOut;
		this.nightOut = nightOut;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public int getFormal() {
		return formal;
	}

	public void setFormal(int formal) {
		this.formal = formal;
	}

	public int getSemiFormal() {
		return semiFormal;
	}

	public void setSemiFormal(int semiFormal) {
		this.semiFormal = semiFormal;
	}

	public int getCasual() {
		return casual;
	}

	public void setCasual(int casual) {
		this.casual = casual;
	}

	public int getDayOut() {
		return dayOut;
	}

	public void setDayOut(int dayOut) {
		this.dayOut = dayOut;
	}

	public int getNightOut() {
		return nightOut;
	}

	public void setNightOut(int nightOut) {
		this.nightOut = nightOut;
	}
	
	public int getPoint(OccasionEnum oe) {
		switch(oe) {
		case Formal:
			return formal;
		case Semi_Formal:
			return semiFormal;
		case Casual:
			return casual;
		case Day_Out:
			return dayOut;
		case Night_Out:
			return nightOut;
		default:
			return casual;
		}
	}
}
