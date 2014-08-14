package com.adl.closetstylist.ui.actionfragment;

import com.adl.closetstylist.ui.ActionDescriptor;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public abstract class ActionFragment extends Fragment{
	public static Fragment getNewActionFragment(ActionDescriptor actionDescriptor) {
		switch (actionDescriptor) {
		case Dashboard: return new DashboardFragment();
		case MyCloset: return new MyClosetFragment();
		case MyLaundryBag: return new MyLaundryBagFragment();
		case OutfitOfTheDay: return new OutfitOfTheDayFragment();
		case Settings:
		case Help:
		default:
			return null;
		}
	}
	
	protected static final String ARG_ACTION_ID = "action_id";
	protected ActionDescriptor actionDescriptor;

	protected ActionFragment(ActionDescriptor actionDescriptor) {
		this.actionDescriptor = actionDescriptor;
		Bundle args = new Bundle();
		args.putInt(ARG_ACTION_ID, actionDescriptor.getId());
		this.setArguments(args);
	}
	
}
