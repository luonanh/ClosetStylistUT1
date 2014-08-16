package com.adl.closetstylist.ui.actionfragment;

import java.util.Locale;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.adl.closetstylist.ItemDataAdapter;
import com.adl.closetstylist.R;
import com.adl.closetstylist.db.ItemDatabaseHelper;
import com.adl.closetstylist.ui.ActionDescriptor;
import com.adl.closetstylist.ui.EditItemActivity;
import com.adl.closetstylist.ui.EditItemActivity.ActionType;
import com.adl.closetstylist.ui.GarmentCategory;
import com.adl.closetstylist.ui.MainActivity;

public class MyClosetFragment extends ActionFragment implements ActionBar.TabListener {
	private static final String TAG = MyClosetFragment.class.getCanonicalName();
	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	private static final String ARG_ACTION_ID = "action_id";
	private SectionsPagerAdapter mSectionsPagerAdapter;
	private ViewPager mViewPager;
	private ItemDatabaseHelper itemDatabaseHelper;
	private ItemDataAdapter itemDataAdapter;

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public MyClosetFragment() {
		super(ActionDescriptor.MyCloset);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		itemDatabaseHelper = new ItemDatabaseHelper(getActivity());
		
		// Set up the action bar.
		final ActionBar actionBar = getActivity().getActionBar();
		actionBar.setNavigationMode(actionDescriptor.getActionBarNavigationMode());
		
		View rootView = inflater.inflate(R.layout.fragment_my_closet,
				container, false);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		actionBar.removeAllTabs();
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
		
		Button addItemBtn = (Button) rootView.findViewById(R.id.btn_additem);
		addItemBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), EditItemActivity.class);
				i.putExtra(EditItemActivity.ACTION_TYPE, ActionType.ADD.name());
                startActivityForResult(i, EditItemActivity.ADD_ITEM_REQUESTCODE);
			}
		});

		// For debug only - Set up options to create default closet
		setHasOptionsMenu(true);
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
		// TODO Auto-generated method stub
		super.onResume();
		((MainActivity)getActivity()).onSectionAttached(getArguments().getInt(
				ARG_ACTION_ID));
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//catch if deleting of an item occurs
		if (requestCode == EditItemActivity.EDIT_ITEM_REQUESTCODE
				&& resultCode == EditItemActivity.DELETE_OK
			||
			//catch if adding of an item occurs
			requestCode == EditItemActivity.ADD_ITEM_REQUESTCODE
			&& resultCode == EditItemActivity.ADD_OK) {
			mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());

			// Set up the ViewPager with the sections adapter.
			int currentItem = mViewPager.getCurrentItem();
			mViewPager.setAdapter(mSectionsPagerAdapter);
			mViewPager.setCurrentItem(currentItem);
		}
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

    // For debug only - Set up options to create default closet
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_my_closet, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.my_closet_menu_create_default_closet_male:
			itemDatabaseHelper.createDefaultDatabaseForMale();
			return true;
		case R.id.my_closet_menu_create_default_closet_female:
			itemDatabaseHelper.createDefaultDatabaseForFemale();
			return true;			
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class
			// below).
			ItemDataListFragment fragment = ItemDataListFragment.newInstance(GarmentCategory.getById(position + 1));
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return GarmentCategory.values().length;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			
			GarmentCategory cat = GarmentCategory.getById(position+1);
			if (cat != null)
				return getString(cat.getLabelResourceId());
			
			return null;
		}
	}
}