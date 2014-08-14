package com.adl.closetstylist.ui.utils;

import android.content.Context;

public class PixelUtils {
	public static float pixelsToSp(Context context, float px) {
	    float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
	    return px/scaledDensity;
	}
	
	public static float spToPixel(Context context, float sp) {
	    float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
	    return sp * scaledDensity;
	}
}
