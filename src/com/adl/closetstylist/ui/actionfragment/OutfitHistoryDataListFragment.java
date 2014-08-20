package com.adl.closetstylist.ui.actionfragment;

import java.util.Date;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.adl.closetstylist.OutfitHistoryData;
import com.adl.closetstylist.OutfitHistoryDataAdapter;
import com.adl.closetstylist.R;
import com.adl.closetstylist.TimeHelper;
import com.adl.closetstylist.db.ItemDatabaseHelper;
import com.adl.closetstylist.ui.ActionDescriptor;

public class OutfitHistoryDataListFragment extends Fragment {
	private static final String TAG = OutfitHistoryDataListFragment.class.getCanonicalName();
	
	private ItemDatabaseHelper itemDatabaseHelper;
	private Cursor cursorToOutfitHistoryDataList;
	private OutfitHistoryDataAdapter ohdAdapter;
	private int tabPosition; 	// tab position in OutfitHistoryFragment
	private long tabTime;		// time in milliseconds associated with the date of the tab in OutfitHistoryFragment
	
	public static OutfitHistoryDataListFragment newInstance(int position) {
		OutfitHistoryDataListFragment result = new OutfitHistoryDataListFragment();
		result.tabPosition = position;
		result.tabTime = tabPositionToTime(result);
		return result;
	}	
	
	/**
	 * This method convert the tab position in the OutfitHistoryFragment to
	 * the corresponding time of the date in milliseconds
	 * @return
	 */
	private static long tabPositionToTime(OutfitHistoryDataListFragment ohdl) {
		return TimeHelper.getTimeNDaysAgo(ohdl.tabPosition);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		itemDatabaseHelper = ItemDatabaseHelper.getInstance(getActivity());
		cursorToOutfitHistoryDataList = itemDatabaseHelper
				.queryOutfitHistoryDataInTimeRange(
						TimeHelper.getStartOfDayInMillis(new Date(this.tabTime)), 
						TimeHelper.getEndOfDayInDateInMillis(new Date(this.tabTime)));
		ohdAdapter = new OutfitHistoryDataAdapter(getActivity(), 
				cursorToOutfitHistoryDataList);

		View rootView = inflater.inflate(
				R.layout.fragment_my_closet_garment_category, 
				container,
				false);
		ListView listView = (ListView) rootView;
		listView.setAdapter(ohdAdapter);
		OnItemClickListener listener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Cursor c = (Cursor) ohdAdapter.getItem(position);
				if (null != c) {
					Log.i(TAG, "Cursor is NOT null");
					OutfitHistoryData ohd = itemDatabaseHelper.getOutfitHistoryDataFromCursor(c);
					OutfitPreviewFragment actionFragment = (OutfitPreviewFragment) ActionFragment.getNewActionFragment(ActionDescriptor.OutfitPreview);
					actionFragment.setOutfit(ohd);
					FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
					fragmentManager.beginTransaction()
							.replace(R.id.container, actionFragment).addToBackStack(null).commit();
					//i.putExtra(Schema.Item.Cols.ID, itemData.getId());
					Log.i(TAG, "id is: " + ohd.getId());
				} else {
					Log.i(TAG, "Cursor is null");
				}
			}
		};
		listView.setOnItemClickListener(listener);
		return rootView;
	}
}
