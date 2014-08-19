package com.adl.closetstylist.ui.actionfragment;


import com.adl.closetstylist.R;
import com.adl.closetstylist.ui.view.OutfitItemView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class OutfitArrayAdapter extends BaseAdapter {
	
	private Context context;

	public OutfitArrayAdapter(Context context) {
		if (context == null)
			throw new IllegalArgumentException("context cannot be null");
		
		this.context = context;
	}
	
	@Override
	public int getCount() {
		return 5;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		OutfitItemView view = null;
		if (convertView != null) {
			view = (OutfitItemView) convertView;
		} else {
			view = new OutfitItemView(context);
		}
		view.getImage1().setImageResource(R.drawable.face_img_placeholder);

		view.getImage2().setImageResource(R.drawable.face_img_placeholder);
		
		view.getImage3().setImageResource(R.drawable.face_img_placeholder);
		
		return view;
	}
	
	public boolean hasStableIds() {
		return true;
	}

}
