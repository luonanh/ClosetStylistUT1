package com.adl.closetstylist.ui.actionfragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.adl.closetstylist.ItemData;
import com.adl.closetstylist.OutfitHistoryData;
import com.adl.closetstylist.OutfitHistoryDataAdapter;
import com.adl.closetstylist.R;
import com.adl.closetstylist.TimeHelper;
import com.adl.closetstylist.db.ItemDatabaseHelper;
import com.adl.closetstylist.db.Schema;
import com.adl.closetstylist.ui.ActionDescriptor;
import com.adl.closetstylist.ui.EditItemActivity;
import com.adl.closetstylist.ui.EditItemActivity.ActionType;

public class OutfitHistoryDataListFragment extends Fragment {
	private static final String TAG = OutfitHistoryDataListFragment.class.getCanonicalName();
	
	private ItemDatabaseHelper itemDatabaseHelper;
	private Cursor cursorToOutfitHistoryDataList;
	private ListAdapter ohdAdapter;

	private int position;
	
	public OutfitHistoryDataListFragment(int position) {
		this.position = position;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		itemDatabaseHelper = ItemDatabaseHelper.getInstance(getActivity());
//		cursorToOutfitHistoryDataList = itemDatabaseHelper
//				.queryOutfitHistoryDataInTimeRange(
//						TimeHelper.getStartOfToday(), 
//						TimeHelper.getEndOfToday());
//		ohdAdapter = new OutfitHistoryDataAdapter(getActivity(), 
//				cursorToOutfitHistoryDataList);
		
		ohdAdapter = new OutfitArrayAdapter(getActivity());

		View rootView = inflater.inflate(R.layout.fragment_my_closet_garment_category, container,
				false);
		ListView listView = (ListView) rootView;
		listView.setAdapter(ohdAdapter);
		OnItemClickListener listener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(getActivity(), "item clicked", Toast.LENGTH_LONG).show();
				OutfitPreviewFragment actionFragment = (OutfitPreviewFragment) ActionFragment.getNewActionFragment(ActionDescriptor.OutfitPreview);
				actionFragment.setOutfit((OutfitHistoryData) view.getTag());
				FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
				fragmentManager.beginTransaction()
						.replace(R.id.container, actionFragment).addToBackStack(null).commit();
			}
		};
		listView.setOnItemClickListener(listener );
		return rootView;
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
