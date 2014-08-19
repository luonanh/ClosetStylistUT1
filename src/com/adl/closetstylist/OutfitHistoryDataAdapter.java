package com.adl.closetstylist;

import com.adl.closetstylist.db.ItemDatabaseHelper;
import com.adl.closetstylist.ui.view.ImageAndTwoLineItem;
import com.adl.closetstylist.ui.view.OutfitItemView;

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
		OutfitItemView view = new OutfitItemView(context);
		return view;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// build an ItemData object from Cursor 
		OutfitHistoryData outfitHistoryData = itemDatabaseHelper.getOutfitHistoryDataFromCursor(cursor);
		
		if (view != null) {
			OutfitItemView v = (OutfitItemView) view;
			
			v.setTag(outfitHistoryData);
			
			v.getImage1().setImageResource(R.drawable.face_img_placeholder);

			v.getImage2().setImageResource(R.drawable.face_img_placeholder);
			
			if (outfitHistoryData.getOutfit().isOuterExist()) {
				v.getImage3().setImageResource(R.drawable.face_img_placeholder);
			}
		}
	}

}
