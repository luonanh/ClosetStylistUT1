package com.adl.closetstylist;

import com.adl.closetstylist.db.ItemDatabaseHelper;
import com.adl.closetstylist.ui.view.ImageAndTwoLineItem;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

public class OutfitHistoryDataAdapter extends CursorAdapter {
	Context context = null;
	ItemDatabaseHelper itemDatabaseHelper;

	public OutfitHistoryDataAdapter(Context context, Cursor c) {
		super(context, c);
		this.context = context;
		itemDatabaseHelper = ItemDatabaseHelper.getInstance(context);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		//OutfitImage view = new OutfitImage(context);
		//return view;
		return null;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// build an ItemData object from Cursor 
		OutfitHistoryData outfitHistoryData = itemDatabaseHelper.getOutfitHistoryDataFromCursor(cursor);
		
		if (view != null) {
			// OutfitImage v = (OutfitImage) view;
			
			// Populate top image

			// Populate bottom image
			
			if (outfitHistoryData.getOutfit().isOuterExist()) {
				// Populate outer image
			}
		}
	}

}
