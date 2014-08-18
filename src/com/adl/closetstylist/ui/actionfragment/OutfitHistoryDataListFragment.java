package com.adl.closetstylist.ui.actionfragment;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.adl.closetstylist.ItemData;
import com.adl.closetstylist.ItemDataAdapter;
import com.adl.closetstylist.R;
import com.adl.closetstylist.db.ItemDatabaseHelper;
import com.adl.closetstylist.db.Schema;
import com.adl.closetstylist.ui.EditItemActivity;
import com.adl.closetstylist.ui.EditItemActivity.ActionType;
import com.adl.closetstylist.ui.GarmentCategory;

public class OutfitHistoryDataListFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	private static final String ARG_GARMENT_CATEGORY_ID = "garment_category_id";
	private static final String TAG = OutfitHistoryDataListFragment.class.getCanonicalName();
	
	private GarmentCategory garmentCategory;
	private ItemDatabaseHelper itemDatabaseHelper;
	private ItemDataAdapter itemDataAdapter;
	private Cursor cursorToItemDataList;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		itemDatabaseHelper = ItemDatabaseHelper.getInstance(getActivity());

		switch (garmentCategory) {
		case JACKET:
			cursorToItemDataList = itemDatabaseHelper.queryAllOuter();
			break;
		case TOP:
			cursorToItemDataList = itemDatabaseHelper.queryAllTopExceptOuter();
			break;
		case BOTTOM:
			cursorToItemDataList = itemDatabaseHelper.queryBottom();
			break;
		default:
			cursorToItemDataList = itemDatabaseHelper.getCursorToAllItemDataRecord();
		}
		itemDataAdapter = new ItemDataAdapter(getActivity(), cursorToItemDataList);

		View rootView = inflater.inflate(R.layout.fragment_my_closet_garment_category, container,
				false);
		ListView listView = (ListView) rootView;
		listView.setAdapter(itemDataAdapter);
		OnItemClickListener listener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent i = new Intent(getActivity(), EditItemActivity.class);
				i.putExtra(EditItemActivity.ACTION_TYPE, ActionType.EDIT.name());
				
				// Retrieve ItemData's Id and pass as Extra of an Intent
				Cursor c = (Cursor) itemDataAdapter.getItem(position);
				if (null != c) {
					Log.i(TAG, "Cursor is NOT null");
					ItemData itemData = ItemDatabaseHelper.getItemDataFromCursor(c);
					i.putExtra(Schema.Item.Cols.ID, itemData.getId());
					Log.i(TAG, "id is: " + itemData.getId());
				} else {
					Log.i(TAG, "Cursor is null");
				}
				//start activity for intent by using parent fragment, return code will be handle
				//in parent fragment's onActivityResult(). Parent fragment in this case is MyClosetFragment
				getParentFragment().startActivityForResult(i, EditItemActivity.EDIT_ITEM_REQUESTCODE);
			}
		};
		listView.setOnItemClickListener(listener );
		return rootView;
	}
	
	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static OutfitHistoryDataListFragment newInstance(GarmentCategory garmentCategory) {
		OutfitHistoryDataListFragment fragment = new OutfitHistoryDataListFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_GARMENT_CATEGORY_ID, garmentCategory.getId());
		fragment.setArguments(args);
		fragment.garmentCategory = garmentCategory;
		return fragment;
	}
}
