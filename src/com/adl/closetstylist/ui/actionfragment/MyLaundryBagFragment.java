package com.adl.closetstylist.ui.actionfragment;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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
import com.adl.closetstylist.ui.ActionDescriptor;
import com.adl.closetstylist.ui.EditItemActivity;
import com.adl.closetstylist.ui.EditItemActivity.ActionType;
import com.adl.closetstylist.ui.MainActivity;

public class MyLaundryBagFragment extends ActionFragment {
	private final static String TAG = MyLaundryBagFragment.class.getCanonicalName();
	
	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	private static final String ARG_ACTION_ID = "action_id";
	private ItemDatabaseHelper itemDatabaseHelper;
	private ItemDataAdapter itemDataAdapter;

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public MyLaundryBagFragment() {
		super(ActionDescriptor.MyLaundryBag);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		itemDatabaseHelper = new ItemDatabaseHelper(getActivity());
		
		// Set up the action bar.
		final ActionBar actionBar = getActivity().getActionBar();
		actionBar.setNavigationMode(actionDescriptor.getActionBarNavigationMode());
				
		View rootView = inflater.inflate(R.layout.fragment_my_laundry_bag,
				container, false);

		ListView listView = (ListView) rootView.findViewById(R.id.garmentlist);
		itemDataAdapter = new ItemDataAdapter(getActivity(), itemDatabaseHelper.queryDirtyItem(true));
		//itemDataAdapter = new ItemDataAdapter(getActivity(), itemDatabaseHelper.getCursorToAllItemDataRecord());
		listView.setAdapter(itemDataAdapter);
		OnItemClickListener listener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent i = new Intent(getActivity(), EditItemActivity.class);
				i.putExtra(EditItemActivity.ACTION_TYPE, ActionType.EDIT.name());
				Cursor c = (Cursor) itemDataAdapter.getItem(position);
				if (null != c) {
					Log.i(TAG, "Cursor is NOT null");
					ItemData itemData = ItemDatabaseHelper.getItemDataFromCursor(c);
					i.putExtra(Schema.Item.Cols.ID, itemData.getId());
					Log.i(TAG, "id is: " + itemData.getId());
				} else {
					Log.i(TAG, "Cursor is null");
				}
                startActivity(i);					
			}
		};
		listView.setOnItemClickListener(listener );
		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments().getInt(
				ARG_ACTION_ID));
	}
	
	@Override
	public void onResume() {
		super.onResume();
		((MainActivity)getActivity()).onSectionAttached(getArguments().getInt(
				ARG_ACTION_ID));
	}
}