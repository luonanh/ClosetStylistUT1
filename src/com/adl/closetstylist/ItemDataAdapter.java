package com.adl.closetstylist;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import com.adl.closetstylist.db.ItemDatabaseHelper;
import com.adl.closetstylist.ui.view.ImageAndTwoLineItem;

public class ItemDataAdapter extends CursorAdapter {
	Context context = null;
	
	public ItemDataAdapter(Context context, Cursor cursor) {
		super(context, cursor);
		this.context = context;
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		ImageAndTwoLineItem view = new ImageAndTwoLineItem(context);
		return view;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		
		// build an ItemData object from Cursor 
		ItemData itemData = ItemDatabaseHelper.getItemDataFromCursor(cursor);
		
		if (view != null) {
			ImageAndTwoLineItem v = (ImageAndTwoLineItem) view;
			
			// Populate name field
			if (itemData.getName().isEmpty()) {
				v.setMainText(context.getString(R.string.my_closet_value_item_name_default_value));
			} else {
				v.setMainText(itemData.getName());
			}
			
			// Populate description field
			if (itemData.getDescription().isEmpty()) {
				v.setSubText(context.getString(R.string.my_closet_value_item_description_default_value));
			} else {
				v.setSubText(itemData.getDescription());
			}
			
			// Populate image
			v.setImage(itemData);
		}
	}
}
