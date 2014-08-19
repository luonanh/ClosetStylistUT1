package com.adl.closetstylist.ui.actionfragment;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.adl.closetstylist.ImageSubSampler;
import com.adl.closetstylist.ItemData;
import com.adl.closetstylist.OutfitHistoryData;
import com.adl.closetstylist.R;
import com.adl.closetstylist.db.ItemDatabaseHelper;
import com.adl.closetstylist.db.Schema;
import com.adl.closetstylist.ui.ActionDescriptor;
import com.adl.closetstylist.ui.EditItemActivity;
import com.adl.closetstylist.ui.EditItemActivity.ActionType;
import com.adl.closetstylist.ui.MainActivity;

public class OutfitPreviewFragment extends ActionFragment {
	private final static String TAG = OutfitPreviewFragment.class.getCanonicalName();
	
	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	private static final String ARG_ACTION_ID = "action_id";
	
	private ItemDatabaseHelper itemDatabaseHelper;
	private Context context;
	private ImageButton top; 	// the image in the topmost, not necessary the image of the "top" ItemData 
	private ImageButton outer; 	// the side image, not necessary the image of the "outer" ItemData
	private ImageButton bottom; // the image in the bottommost
	private ItemData topItem; 	// the current top ItemData, its image is not necessary on the topmost part. Tied to topIndex after clicking top arrows, but tied to outfit.getTop() after clicking double arrows. 
	private ItemData bottomItem;// the current bottom ItemData. Tied to bottomIndex after clicking top arrows, but tied to outfit.getOuter() after clicking double arrows.
	private ItemData outerItem; // the current outer ItemData, its image is not necessary on the side. Tied to outerIndex after clicking top arrows, but tied to outfit.getBottom() after clicking double arrows.
	private boolean upperImageIsTop = true; // true - traversing through top; false - traversing through outer

	private OutfitHistoryData outfitHistoryData;

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public OutfitPreviewFragment() {
		super(ActionDescriptor.OutfitPreview);
	}
	
	public void setOutfit(OutfitHistoryData outfitHistoryData) {
		this.outfitHistoryData = outfitHistoryData;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		itemDatabaseHelper = ItemDatabaseHelper.getInstance(getActivity());
		context = getActivity().getApplicationContext();

		// Set up the action bar.
		final ActionBar actionBar = getActivity().getActionBar();

		final View rootView = inflater.inflate(R.layout.fragment_outfit_preview,
				container, false);

		setupRelationBetweenTopPartAndOuterPart(rootView);
		
		//setup view with data from outfitHistoryData
		
		
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		
		setHasOptionsMenu(true);
		return rootView;
	}

	/**
	 * This method handle the following events
	 * Click on ImageButton top - go to view activity with the current item display in the top.
	 * Click on ImageButton bottom - go to view activity with the current item display in the bottom.
	 * Click on ImageButton outer - swap the top with outer.
	 */
	private void setupRelationBetweenTopPartAndOuterPart(final View rootView) {
		top = (ImageButton) rootView.findViewById(R.id.top_garment_image);
		outer = (ImageButton) rootView.findViewById(R.id.top_garment_outer_image);
		bottom = (ImageButton) rootView.findViewById(R.id.bottom_garment_image);
		
		outer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				swapTopAndOuterInUpperImage();
			}
		});
		
		top.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), EditItemActivity.class);
				i.putExtra(EditItemActivity.ACTION_TYPE, ActionType.VIEW.name());
				
				// Retrieve ItemData's Id and pass as Extra of an Intent
				if (upperImageIsTop) {
					if (topItem != null) {
						i.putExtra(Schema.Item.Cols.ID, topItem.getId());
						startActivity(i);
					}
				} else {
					if (outerItem != null) {
						i.putExtra(Schema.Item.Cols.ID, outerItem.getId());
						startActivity(i);						
					}
				}
			}
		});
		
		bottom.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), EditItemActivity.class);
				i.putExtra(EditItemActivity.ACTION_TYPE, ActionType.VIEW.name());
				
				// Retrieve ItemData's Id and pass as Extra of an Intent
				if (bottomItem != null) {
					i.putExtra(Schema.Item.Cols.ID, bottomItem.getId());
					startActivity(i);
				}
			}
		});
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
		((MainActivity) getActivity()).onSectionAttached(getArguments().getInt(
				ARG_ACTION_ID));
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_outfit_preview, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_share:
			Toast.makeText(getActivity(), "Share", Toast.LENGTH_LONG).show();
			return true;
		case R.id.menu_delete:
			Toast.makeText(getActivity(), "Delete", Toast.LENGTH_LONG).show();
			return true;			
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * This method 
	 *  - swaps the ItemData objects in the ImageButton outer and the ImageButton top
	 *  - DOES NOT update the topItem and outerItem
	 *  - update tag of ImageButton top and outer 
	 */
	private void swapTopAndOuterInUpperImage() {
		ItemData oldOuterItem = (ItemData) outer.getTag();
		ItemData oldTopItem = (ItemData) top.getTag();
		if ((oldOuterItem != null) && (oldTopItem != null)) {
			upperImageIsTop = !upperImageIsTop; // toggle the currentTop
			new ImageSubSampler(context).subSampleCroppedUri(oldOuterItem, top, context);
			top.setTag(oldOuterItem);
			new ImageSubSampler(context).subSampleCroppedUri(oldTopItem, outer, context);
			outer.setTag(oldTopItem);
		}
	}
	
}