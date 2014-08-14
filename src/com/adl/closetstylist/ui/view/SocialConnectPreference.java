package com.adl.closetstylist.ui.view;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adl.closetstylist.R;

public class SocialConnectPreference extends Preference {

	public SocialConnectPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public SocialConnectPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SocialConnectPreference(Context context) {
		super(context);
	}

	@Override
	protected View onCreateView(ViewGroup parent) {
		View layout = null;

		try {
			LayoutInflater mInflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			layout = mInflater.inflate(
					R.layout.preference_social_connect, parent, false);
		} catch (Exception e) {
		}

		return layout;
	}
}
