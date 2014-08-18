package com.adl.closetstylist;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class OutfitHistoryData {

	private long id;
	private Outfit outfit;
	private Long time;	// time that user choose to wear this outfit
	
	public OutfitHistoryData(Outfit o) {
		this.outfit = o;
		time = System.currentTimeMillis();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Outfit getOutfit() {
		return outfit;
	}

	public void setOutfit(Outfit outfit) {
		this.outfit = outfit;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}
	
	public String getTimeInDateFormat() {
		/* http://developer.android.com/reference/java/text/SimpleDateFormat.html
		DateFormat[] formats = new DateFormat[] {
			DateFormat.getDateInstance(),
			DateFormat.getDateTimeInstance(),
			DateFormat.getTimeInstance(),
		};
		for (DateFormat df : formats) {
			System.out.println(df.format(new Date(0)));
			df.setTimeZone(TimeZone.getTimeZone("UTC"));
			System.out.println(df.format(new Date(0)));
		}
		*/
		Date d = new Date(this.time);
		SimpleDateFormat sdf = new SimpleDateFormat("YYY-MM-DD HH:MM:SS", Locale.US);
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		return sdf.format(d);
	}

	@Override
	public String toString() {
		String outerStr = null;
		if (this.outfit.isOuterExist()) {
			outerStr = " -- Outer -- " + this.outfit.getOuter().toString();
		}
		
		return "Top -- " + this.outfit.getTop().toString() + " -- Bottom -- " + this.outfit.getBottom().toString() + outerStr;
	}
	
	
	
}
