package com.adl.closetstylist.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import com.adl.closetstylist.R;

public enum ActionDescriptor {
	Dashboard(1, R.string.dashboard, R.drawable.icn_dashboard, ActionBar.NAVIGATION_MODE_STANDARD),
	OutfitOfTheDay(2, R.string.outfitoftheday, R.drawable.icn_outfitofday, ActionBar.NAVIGATION_MODE_STANDARD),
	OutfitHistory(6, R.string.outfit_history, R.drawable.icn_history, ActionBar.NAVIGATION_MODE_STANDARD),
	MyCloset(3, R.string.mycloset, R.drawable.icn_mycloset, ActionBar.NAVIGATION_MODE_TABS),
	MyLaundryBag(4, R.string.mylaundrybag, R.drawable.icn_laundry, ActionBar.NAVIGATION_MODE_STANDARD),
	Settings(5, R.string.settings, R.drawable.icn_settings, ActionBar.NAVIGATION_MODE_STANDARD),
	Help(7, R.string.help, R.drawable.icn_help, ActionBar.NAVIGATION_MODE_STANDARD),
	OutfitPreview(8, R.string.outfit_preview, R.drawable.icn_outfitofday);
	
	private int id;
	private int labelResourceId;
	private int iconResourceId;
	private int actionBarNavigationMode;
//	private Fragment fragment;

	private ActionDescriptor(int id, int labelResourceId, int iconResourceId) {
		this(id, labelResourceId, iconResourceId, ActionBar.NAVIGATION_MODE_STANDARD);
	}
	
	private ActionDescriptor(int id, int labelResourceId, int iconResourceId, int actionBarNavigationMode) {
		this.id = id;
		this.labelResourceId = labelResourceId;
		this.iconResourceId = iconResourceId;
		this.actionBarNavigationMode = actionBarNavigationMode;
	}
	
	public static ActionDescriptor getById(int id) {
		for (ActionDescriptor actDesc : values())
			if (actDesc.getId() == id)
				return actDesc;
		
		return null;
	}

	public int getLabelResourceId() {
		return labelResourceId;
	}

	public int getIconResourceId() {
		return iconResourceId;
	}

	public int getId() {
		return id;
	}
	
	public int getPostion() {
		return id-1;
	}

	public int getActionBarNavigationMode() {
		return actionBarNavigationMode;
	}
	
	public static ActionDescriptor[] getDisplayableActions() {
		List<ActionDescriptor> result = new ArrayList<ActionDescriptor>();
		for (ActionDescriptor action : values()) {
			switch (action) {
			case OutfitPreview:
				break;
			default:
				result.add(action);
			}
		}
		
		return result.toArray(new ActionDescriptor[result.size()]);
	}

//	public Fragment getFragment() {
//		return fragment;
//	}
//
//	public void setFragment(Fragment fragment) {
//		this.fragment = fragment;
//	}

}