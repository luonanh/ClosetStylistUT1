package com.adl.closetstylist.ui.actionfragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adl.closetstylist.OutfitHistoryData;
import com.adl.closetstylist.OutfitHistoryDataAdapter;
import com.adl.closetstylist.TimeHelper;
import com.adl.closetstylist.db.ItemDatabaseHelper;

public class OutfitHistoryDataListFragment extends Fragment {
	private static final String TAG = OutfitHistoryDataListFragment.class.getCanonicalName();
	
	private ItemDatabaseHelper itemDatabaseHelper;
	private Cursor cursorToOutfitHistoryDataList;
	private OutfitHistoryDataAdapter ohdAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		itemDatabaseHelper = ItemDatabaseHelper.getInstance(getActivity());
		cursorToOutfitHistoryDataList = itemDatabaseHelper
				.queryOutfitHistoryDataInTimeRange(
						TimeHelper.getStartOfToday(), 
						TimeHelper.getEndOfToday());
		ohdAdapter = new OutfitHistoryDataAdapter(getActivity(), 
				cursorToOutfitHistoryDataList);

		//listView.setAdapter(itemDataAdapter);
		return super.onCreateView(inflater, container, savedInstanceState);
	}


	/**
	 * This handler will be called by the onItemClick of listView
	 */
	private void clickHandler(int position) {
		Cursor c = (Cursor) ohdAdapter.getItem(position);
		if (null != c) {
			Log.i(TAG, "Cursor is NOT null");
			OutfitHistoryData ohd = itemDatabaseHelper.getOutfitHistoryDataFromCursor(c);
			//i.putExtra(Schema.Item.Cols.ID, itemData.getId());
			Log.i(TAG, "id is: " + ohd.getId());
		} else {
			Log.i(TAG, "Cursor is null");
		}
		//getParentFragment().startActivityForResult(i, EditItemActivity.EDIT_ITEM_REQUESTCODE);
	}
}
