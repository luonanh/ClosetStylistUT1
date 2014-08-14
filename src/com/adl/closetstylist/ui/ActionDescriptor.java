package com.adl.closetstylist.ui;

import android.app.ActionBar;
import com.adl.closetstylist.R;

public enum ActionDescriptor {
	Dashboard(1, R.string.dashboard, R.drawable.icn_dashboard, ActionBar.NAVIGATION_MODE_STANDARD),
	OutfitOfTheDay(2, R.string.outfitoftheday, R.drawable.icn_outfitofday, ActionBar.NAVIGATION_MODE_STANDARD),
	MyCloset(3, R.string.mycloset, R.drawable.icn_mycloset, ActionBar.NAVIGATION_MODE_TABS),
	MyLaundryBag(4, R.string.mylaundrybag, R.drawable.icn_laundry, ActionBar.NAVIGATION_MODE_STANDARD),
	Settings(5, R.string.settings, R.drawable.icn_settings, ActionBar.NAVIGATION_MODE_STANDARD),
	Help(6, R.string.help, R.drawable.icn_help, ActionBar.NAVIGATION_MODE_STANDARD);
	
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
		switch (id) {
		case 1: return Dashboard;
		case 2: return OutfitOfTheDay;
		case 3: return MyCloset;
		case 4: return MyLaundryBag;
		case 5: return Settings;
		case 6: return Help;
		default:
			return null;
		}
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

//	public Fragment getFragment() {
//		return fragment;
//	}
//
//	public void setFragment(Fragment fragment) {
//		this.fragment = fragment;
//	}

}