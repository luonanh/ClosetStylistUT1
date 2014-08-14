package com.adl.closetstylist.ui.actionfragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.adl.closetstylist.CurrentWeatherInfo;
import com.adl.closetstylist.ImageSubSampler;
import com.adl.closetstylist.ItemData;
import com.adl.closetstylist.Outfit;
import com.adl.closetstylist.R;
import com.adl.closetstylist.UserProfile;
import com.adl.closetstylist.WeatherInfo;
import com.adl.closetstylist.db.ItemDatabaseHelper;
import com.adl.closetstylist.db.Schema;
import com.adl.closetstylist.match.clothes.ClothesMatching;
import com.adl.closetstylist.match.clothes.ClothesMatchingFactory;
import com.adl.closetstylist.match.clothes.ClothesMatchingFactoryFemale;
import com.adl.closetstylist.match.clothes.ClothesMatchingFactoryMale;
import com.adl.closetstylist.match.clothes.OccasionEnum;
import com.adl.closetstylist.ui.ActionDescriptor;
import com.adl.closetstylist.ui.EditItemActivity;
import com.adl.closetstylist.ui.EditItemActivity.ActionType;
import com.adl.closetstylist.ui.MainActivity;

public class OutfitOfTheDayFragment extends ActionFragment {
	private final static String TAG = OutfitOfTheDayFragment.class.getCanonicalName();
	
	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	private static final String ARG_ACTION_ID = "action_id";
	
	private ItemDatabaseHelper itemDatabaseHelper;
	private Context context;
	private List<Outfit> outfit;
	private int outfitIndex;
	private ImageButton top;
	private ImageButton outer;
	private ImageButton bottom;
	private ItemData topItem;
	private ItemData bottomItem;
	private ItemData outerItem;
	private boolean upperImageIsTop = true; // true - traversing through top; false - traversing through outer
	private TextView rank;
	private TextView score;
	private int topIndex;
	private int outerIndex;
	private int bottomIndex;
	private List<ItemData> topList;
	private List<ItemData> outerList;
	private List<ItemData> bottomList;
	//private TextC

	enum BottomAction {
		OCCATION(R.id.btn_occation, R.id.menu_occation),
		PREVIOUS(R.id.btn_previous_outfit),
		NEXT(R.id.btn_next_outfit),
		SHARE(R.id.btn_share),
		WEAR(R.id.btn_wear),
		NONE(R.id.outfitoftheday_actionbar);

		OnClickListener clickListener = null;

		private int componentId;

		private int popupViewId;

		private View popupContainerView;

		private BottomAction(int componentId) {
			this(componentId, 0);
		}
		
		private BottomAction(int componentId, int popupViewId) {
			this.componentId = componentId;
			this.popupViewId = popupViewId;
		}
		
		public int getComponentId() {
			return componentId;
		}
		
		public int getPopupViewId() {
			return popupViewId;
		}

		public View getPopupContainerView() {
			return popupContainerView;
		}

		public void setOnClickListener(OnClickListener clickListener) {
			this.clickListener = clickListener;
		}

		private boolean init_internal(final View actionBarView,
				final View popupContainerView) {
			View view = actionBarView.findViewById(componentId);
			if (view == null)
				return false;

			this.popupContainerView = popupContainerView;

			if (popupViewId != 0) {
				view.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (BottomAction.this.isPopupShowed())
							hidePopupView();
						else {
							hideAllPopupViews();
							showPopupView();
						}
					}
				});
			} else {
				view.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						hideAllPopupViews();
						if (clickListener != null)
							clickListener.onClick(v);
					}
				});
			}

			return true;
		}

		public void hidePopupView() {
			if (popupViewId != 0) {
				View view = popupContainerView.findViewById(popupViewId);
				view.setVisibility(View.GONE);
				popupContainerView.setVisibility(View.GONE);
			}
		}

		public void showPopupView() {
			hideAllPopupViews();
			if (popupViewId != 0) {
				View view = popupContainerView.findViewById(popupViewId);
				view.setVisibility(View.VISIBLE);
				popupContainerView.setVisibility(View.VISIBLE);
			}
		}
		
		public boolean isPopupShowed() {
			if (popupViewId == 0)
				return false;
			View view = popupContainerView.findViewById(popupViewId);
			return view.getVisibility() == View.VISIBLE;
		}

		public static void hideAllPopupViews() {
			for (BottomAction action : BottomAction.values()) {
				action.hidePopupView();
			}
		}
		
		public static void init(final View actionBarView,
				final View popupContainerView, Map<BottomAction, ExtendActionInit> extendInitMap) {
			for (BottomAction action : BottomAction.values()) {
				action.init_internal(actionBarView, popupContainerView);
				ExtendActionInit extendActionInit = extendInitMap.get(action);
				if (extendActionInit != null)
					extendActionInit.extendInit(action);
			}
			
			popupContainerView.setOnClickListener(new OnClickListener() {
	
				@Override
				public void onClick(View v) {
					hideAllPopupViews();
				}
			});
		}
	}

	interface ExtendActionInit {
		void extendInit(BottomAction action);
	}
	
	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public OutfitOfTheDayFragment() {
		super(ActionDescriptor.OutfitOfTheDay);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		itemDatabaseHelper = new ItemDatabaseHelper(getActivity());
		context = getActivity().getApplicationContext();

		// Set up the action bar.
		final ActionBar actionBar = getActivity().getActionBar();
		actionBar.setNavigationMode(actionDescriptor
				.getActionBarNavigationMode());

		final View rootView = inflater.inflate(R.layout.fragment_outfitoftheday,
				container, false);

		
		initBottomActionBar(rootView);
		
		setupRelationBetweenTopPartAndOuterPart(rootView);
		
		initLeftRightButtonInTopAndBottomPart(rootView);
		
		// get the views of rank and score
		rank = (TextView) rootView.findViewById(R.id.outfitoftheday_rank);
		score = (TextView) rootView.findViewById(R.id.outfitoftheday_score);
		score.setTextColor(Color.MAGENTA);
		
		resetTopForUpperImage();
		resetOuterForUpperImage();
		resetBottomForLowerImage();
		
		// start clothes matching process
		outfitIndex = 0;
		new RankOutfitTask().execute(OccasionEnum.Casual);
		return rootView;
	}

	private void initLeftRightButtonInTopAndBottomPart(final View rootView) {
		View topPartLeftBtn = rootView.findViewById(R.id.outfitoftheday_toppart).findViewById(R.id.btn_sliding_left);
		View topPartRightBtn = rootView.findViewById(R.id.outfitoftheday_toppart).findViewById(R.id.btn_sliding_right);
		View bottomPartLeftBtn = rootView.findViewById(R.id.outfitoftheday_bottompart).findViewById(R.id.btn_sliding_left);
		View bottomPartRightBtn = rootView.findViewById(R.id.outfitoftheday_bottompart).findViewById(R.id.btn_sliding_right);
		
		//top part buttons
		topPartLeftBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (upperImageIsTop) {
					if (topIndex <= 0) {
						topIndex = topList.size() - 1;
					} else {
						topIndex--;
					}
					updateUpperImage(topList.get(topIndex));
				} else {
					if (outerIndex <= 0) {
						outerIndex = outerList.size() - 1;
					} else {
						outerIndex--;
					}
					updateUpperImage(outerList.get(outerIndex));
				}
			}
		});
		
		topPartRightBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (upperImageIsTop) {
					if (topIndex >= (topList.size() - 1)) {
						topIndex = 0;
					} else {
						topIndex++;
					}
					updateUpperImage(topList.get(topIndex));
				} else {
					if (outerIndex >= (outerList.size() - 1)) {
						outerIndex = 0;
					} else {
						outerIndex++;
					}
					updateUpperImage(outerList.get(outerIndex));
				}
			}
		});
		
		
		//bottom part buttons		
		bottomPartLeftBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (bottomIndex <= 0) {
					bottomIndex = bottomList.size() - 1;
				} else {
					bottomIndex--;
				}
				updateLowerImage(bottomList.get(bottomIndex));			
			}
		});
		
		bottomPartRightBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (bottomIndex >= (bottomList.size() - 1)) {
					bottomIndex = 0;
				} else {
					bottomIndex++;
				}
				updateLowerImage(bottomList.get(bottomIndex));
			}
		});
	}

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

	private void initBottomActionBar(final View rootView) {
		Map<BottomAction, ExtendActionInit> extendInitMap = new HashMap<OutfitOfTheDayFragment.BottomAction, OutfitOfTheDayFragment.ExtendActionInit>();
		
		extendInitMap.put(BottomAction.OCCATION, new ExtendActionInit() {
			
			@Override
			public void extendInit(final BottomAction action) {
				if (action != BottomAction.OCCATION)
					return;
				
				ListView listView = (ListView) action.getPopupContainerView()
						.findViewById(action.getPopupViewId());
				listView.setAdapter(new ArrayAdapter<String>(getActivity(),
						R.layout.simple_list_item_1,
						OccasionEnum.getAllOccasionEnumString()));
				listView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						outfitIndex = 0;
						OccasionEnum oe = OccasionEnum.valueOf((String) parent.getItemAtPosition(position));
						Log.i(TAG, "The chosen OccasionEnum is: " + oe.toString());
						new RankOutfitTask().execute(oe);
						
						//hide the list
						action.hidePopupView();
					}
				});
			}
		});
		
		extendInitMap.put(BottomAction.PREVIOUS, new ExtendActionInit() {
			
			@Override
			public void extendInit(BottomAction action) {
				if (action != BottomAction.PREVIOUS)
					return;
				
				View view = rootView.findViewById(action.getComponentId());
				view.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if (null == outfit) {
							Toast.makeText(context, R.string.outfit_message_no_outfit, 
									Toast.LENGTH_SHORT).show();
						} else {
							if (outfitIndex <= 0) {
								outfitIndex = outfit.size() - 1;
							} else {
								// swap back the top and outer if outer is in the upper image
								if (!upperImageIsTop) {
									swapTopAndOuterInUpperImage();
								}
								
								outfitIndex--;
								updateTopBottomOuter();							
							}
						}
					}
				});
			}
		});

		extendInitMap.put(BottomAction.NEXT, new ExtendActionInit() {
			
			@Override
			public void extendInit(BottomAction action) {
				if (action != BottomAction.NEXT)
					return;
				
				View view = rootView.findViewById(action.getComponentId());
				view.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if (null == outfit) {
							Toast.makeText(context, R.string.outfit_message_no_outfit, 
									Toast.LENGTH_SHORT).show();
						} else {
							if (outfitIndex >= (outfit.size() - 1)) {
								outfitIndex = 0;
							} else {
								outfitIndex++;
								updateTopBottomOuter();
							}
						}
					}
				});
			}
		});
		
		extendInitMap.put(BottomAction.WEAR, new ExtendActionInit() {
			
			@Override
			public void extendInit(BottomAction action) {
				if (action != BottomAction.WEAR)
					return;
				
				View view = rootView.findViewById(action.getComponentId());
				view.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						wearOnClickHandler();
					}
				});
			}
		});

		BottomAction.init(rootView.findViewById(R.id.outfitoftheday_actionbar), 
				rootView.findViewById(R.id.menu_layout), extendInitMap);
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

	private class RankOutfitTask extends AsyncTask<OccasionEnum, Void, List<Outfit>>{
		private ProgressDialog dialog;
		private WeatherInfo wi;
		private UserProfile up;
		
		public RankOutfitTask() {
			super();
			
			if (CurrentWeatherInfo.isCurrentWeatherInfoToday()) { // validate CurrentWeatherInfo
				wi = CurrentWeatherInfo.getCurrentWeatherInfo();
			} else {
				wi = null;
			}
			
			// obtain UserProfile
			up = itemDatabaseHelper.getCurrentUserProfile();
			
			// get exception when passed in context to the ProgressDialog
			// constructor. Change to OutfitActivity.this per 
			// http://stackoverflow.com/questions/19024940/android-error-unable-to-add-window-token-null-is-not-for-an-application
			dialog = new ProgressDialog(getActivity());
		}

		@Override
		protected void onPreExecute() {
			
			if ((wi != null) && (up != null)) {
				dialog.setMessage("Searching for the right outfit");
			} else {
				dialog.setMessage("Cannot obtain the necessary information!");
			}
			dialog.show();
			
		}

		@Override
		protected List<Outfit> doInBackground(OccasionEnum... params) {
			if ((wi != null) && (up != null)) {
				/*
				dialog.setMessage("Searching for the right outfit");
				dialog.show();
				*/
				
				ClothesMatchingFactory cmf;
				switch (up.getGender()) {
				case MALE:
					cmf = new ClothesMatchingFactoryMale();
					break;
				case FEMALE:
					cmf = new ClothesMatchingFactoryFemale();
					break;
				default:
					cmf = new ClothesMatchingFactoryMale();
					break;
				}
				/*
				ClothesMatching cm = cmf.newInstance(itemDatabaseHelper, 
						wi, up, OccasionEnum.Casual);
						*/
				ClothesMatching cm = cmf.newInstance(itemDatabaseHelper, 
						wi, up, params[0]);
				return cm.match(wi, itemDatabaseHelper, up);
			} else {
				/*
				dialog.setMessage("Cannot obtain the necessary information!");
				dialog.show();
				*/
				return null;
			}
		}

		@Override
		protected void onPostExecute(List<Outfit> result) {
			if (null != result) {				
				outfit = result; // update items in OutfitOfTheDayFragment
				outfitIndex = 0; // position to keep track of
				Log.i(TAG, "Total number of items in the List of Outfit is: " + outfit.size());
				if (0 == outfit.size()) {
					Toast.makeText(context, R.string.outfit_message_no_top, 
							Toast.LENGTH_SHORT).show();						
				} else {
					updateTopBottomOuter();				
				}
				
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
			} else {
				Toast.makeText(context, R.string.outfit_message_no_outfit, 
						Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	/**
	 * This method update the rank and score of the current outfit when
	 * user traverses through the list of suggested outfits.
	 */
	private void updateRankAndScore() {
		score.setText(String.valueOf(outfit.get(outfitIndex).getScore()));
		rank.setText(String.valueOf(outfitIndex + 1) + "/" + String.valueOf(outfit.size()));
	}
	
	/**
	 * This method swaps the ItemData objects in the outer image and the top image
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
	
	/**
	 * As users traverse along the list of suggested outfits, this method
	 * updates all of the fields in this fragment. 
	 */
	private void updateTopBottomOuter() {
		topItem = outfit.get(outfitIndex).getTop();
		new ImageSubSampler(context).subSampleCroppedUri(topItem, top, context);
		top.setTag(topItem);
		bottomItem = outfit.get(outfitIndex).getBottom();
		new ImageSubSampler(context).subSampleCroppedUri(bottomItem, bottom, context);
		outerItem = outfit.get(outfitIndex).getOuter();
		outer.setTag(outerItem);
		if (outfit.get(outfitIndex).isOuterExist()) {
			Log.i(TAG, "Score of the current outfit: " + outfit.get(outfitIndex).getScore() + ". There is outer.");
			new ImageSubSampler(context).subSampleCroppedUri(outerItem, outer, context);
			outer.setVisibility(View.VISIBLE);
		} else {
			Log.i(TAG, "Score of the current outfit: " + outfit.get(outfitIndex).getScore() + ". There is NO outer.");
			//outer.setImageResource(0); // clear outer image
			outer.setVisibility(View.GONE);
		}
		updateRankAndScore();
	}
	
	/**
	 * This method resets the counter and the list of clean top ItemData objects.
	 */
	private void resetTopForUpperImage() {
		topIndex = 0;
		topList = itemDatabaseHelper.getAllTopExceptOuterPerDirty(false);
	}

	/**
	 * This method resets the counter and the list of clean outer ItemData objects.
	 */
	private void resetOuterForUpperImage() {
		outerIndex = 0;
		outerList = itemDatabaseHelper.getAllOuterPerDirty(false);
	}
	
	/**
	 * This method resets the counter and the list of clean bottom ItemData objects.
	 */
	private void resetBottomForLowerImage() {
		bottomIndex = 0;
		bottomList = itemDatabaseHelper.getAllBottomPerDirty(false);
	}
	
	/**
	 * This method updates the upper image as user traverses through the list
	 * using the backward and forward arrow buttons for the top.
	 */
	private void updateUpperImage(ItemData item) {
		new ImageSubSampler(context).subSampleCroppedUri(item, top, context);
		top.setTag(item);
	}

	/**
	 * This method updates the lower image as user traverses through the list
	 * using the backward and forward arrow buttons for the bottom.
	 */
	private void updateLowerImage(ItemData item) {
		new ImageSubSampler(context).subSampleCroppedUri(item, bottom, context);
		top.setTag(item);
	}
	
	/**
	 * This method handles the Wear button.
	 */
	private void wearOnClickHandler() {
		if (null != outfit) {
			if (null != outfit.get(outfitIndex).getTop()) {
				ItemData temp = outfit.get(outfitIndex).getTop();
				temp.incWornTime();
				itemDatabaseHelper.updateItemDataRecord(temp);
				resetTopForUpperImage();
			}
			
			if (null != outfit.get(outfitIndex).getBottom()) {
				ItemData temp = outfit.get(outfitIndex).getBottom();
				temp.incWornTime();
				itemDatabaseHelper.updateItemDataRecord(temp);
				resetBottomForLowerImage();
			}

			if (null != outfit.get(outfitIndex).getOuter()) {
				ItemData temp = outfit.get(outfitIndex).getOuter();
				temp.incWornTime();
				itemDatabaseHelper.updateItemDataRecord(temp);
				resetOuterForUpperImage();
			}
		
			Toast.makeText(context, "This outfit is chosen", 
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(context, R.string.outfit_message_no_outfit, 
					Toast.LENGTH_SHORT).show();
		}
	}
}