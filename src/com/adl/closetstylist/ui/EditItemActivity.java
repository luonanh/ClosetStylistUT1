package com.adl.closetstylist.ui;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.adl.closetstylist.GenderEnum;
import com.adl.closetstylist.ImageSubSampler;
import com.adl.closetstylist.ItemCategoryEnum;
import com.adl.closetstylist.ItemColorEnum;
import com.adl.closetstylist.ItemData;
import com.adl.closetstylist.ItemMaterialEnum;
import com.adl.closetstylist.ItemStyleEnum;
import com.adl.closetstylist.R;
import com.adl.closetstylist.UserProfile;
import com.adl.closetstylist.db.ItemDatabaseHelper;
import com.adl.closetstylist.db.Schema;
import com.adl.closetstylist.storage.StorageFactory;
import com.adl.closetstylist.storage.StorageInterface;
import com.adl.closetstylist.storage.sd.SDCardStorageFactory;
import com.adl.closetstylist.ui.view.ArrayAdapter;

public class EditItemActivity extends Activity {
	public static final int DELETE_OK = 100;
	public static final int ADD_OK = 101;
	public static final int EDIT_ITEM_REQUESTCODE = 1;
	public static final int ADD_ITEM_REQUESTCODE = 2;	
	public static final String ACTION_TYPE = ActionType.class.getName();	
	static final int CAMERA_PIC_REQUEST_FOR_EDIT = 1;
	static final int CROP_FROM_CAMERA_FOR_EDIT = 2;
	static final int EDIT_FROM_FILE_FOR_EDIT = 3;
	static final int PICK_FROM_GALLERY_FOR_EDIT = 4;
	static final int CROP_FROM_GALLERY_FOR_EDIT = 5;
	static final int CAMERA_PIC_REQUEST_FOR_ADD = 6;
	static final int CROP_FROM_CAMERA_FOR_ADD = 7;
	private static final String TAG = EditItemActivity.class.getCanonicalName();	
	
	private ItemDatabaseHelper itemDatabaseHelper;
	private ItemData itemData;
	private Context context;
	private StorageInterface storage;
	private GenderEnum gender = GenderEnum.MALE;
	private Uri newImagePath = null;
	private Uri newCropImagePath = null;
	private Uri newEditImagePath = null;
	private Uri newGalleryImagePath = null;
	private Uri newCropGalleryImagePath = null;
	private CropImageStatus cropStatus = CropImageStatus.NO_CHANGE;
	private Uri imagePathForAdd = null;
	private Uri cropImagePathForAdd = null;
	private ImageView imageView;
	private UserProfile up;
	private ActionType actionType = null;

	enum SpinnerValue {
		CATEGORY(R.id.item_category, ItemCategoryEnum.getAllItemCategoryEnumString()),
		MATERIAL(R.id.item_material, ItemMaterialEnum.getAllItemMaterialEnumString()),
		COLOR(R.id.item_color, ItemColorEnum.getAllItemColorEnumString()),
		STYLE(R.id.item_style, Arrays.asList(new String[] {"-Please select category-", "-Please select category-"}));

		private int id;
		private List<String> values;

		private SpinnerValue(int id, List<String> values) {
			this.id = id;
			this.values = values;
		}

		public List<String> getValues() {
			return values;
		}

		public int getId() {
			return id;
		}
	}

	public enum ActionType {
		EDIT, VIEW, ADD;
		
		public int getID() {
			return ordinal();
		}
		
		public ActionType getByID(int id) {
			if (id < 0 || id >= ActionType.values().length)
				return null;
			return ActionType.values()[id];
		}
	}
	
	private enum CropImageStatus {
		NO_CHANGE, NEW_IMAGE_NEWER, EDIT_IMAGE_NEWER
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edititem);

		itemDatabaseHelper = ItemDatabaseHelper.getInstance(this);
		context = getApplicationContext();
		up = itemDatabaseHelper.getCurrentUserProfile();
		
		StorageFactory storageFactory = new SDCardStorageFactory();
		storage = storageFactory.getInstance();
		
		UserProfile up = itemDatabaseHelper.getCurrentUserProfile();
		gender = up.getGender();
		
		// This ImageView is used by all activities 
		imageView = (ImageView) findViewById(R.id.item_image);
		
		initSpinner();
		initListerner();
		
		setupActionHandler();
	}

	/**
	 * Set up all image handlers, button handlers.
	 */
	private void setupActionHandler() {

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String actionTypeStr = extras.getString(ACTION_TYPE);
			try {
				actionType = ActionType.valueOf(actionTypeStr);
			} catch (Exception e) {
				//fine, we don't have an action type here, set it default to ADD
				actionType = ActionType.ADD;
			}
		} else {
			//fine, we don't have an action type here either, set it default to ADD
			actionType = ActionType.ADD;
		}
		
		View resetBtn = findViewById(R.id.btn_reset);
		View deleteBtn = findViewById(R.id.btn_delete);
		View saveBtn = findViewById(R.id.btn_save);
		View newImageBtn = findViewById(R.id.btn_add_item_image);
		CheckBox isDirtyCheckbox = (CheckBox) findViewById(R.id.item_dirty);
		
		switch (actionType) {
		case VIEW:
			setTitle("View Item");
			
			//remove add image button
			newImageBtn.setVisibility(View.GONE);
			
			//remove abilily to edit/add item's image for VIEW Action
			newImageBtn.setOnClickListener(null);
			imageView.setOnLongClickListener(null);
			
			//remove save/reset buttons
			findViewById(R.id.button_section).setVisibility(View.GONE);
			
			// set information fields to disabled
			int[] ids = new int[] {
					R.id.item_name,
					R.id.item_description,
					R.id.item_age,
					R.id.item_category,
					R.id.item_color,
					R.id.item_description,
					R.id.item_image,
					R.id.item_material,
					R.id.item_max_temp,
					R.id.item_min_temp,
					R.id.item_brand,
					R.id.item_style,
					R.id.item_dirty
			};
			for (int viewId : ids) {
				View view = findViewById(viewId);
				view.setEnabled(false);
				view.setFocusable(false);
			}
			
			itemData = getItemDataFromIntent();
			setItemData(itemData);
			break;
		case EDIT:
			setTitle("Edit Item");
			
			itemData = getItemDataFromIntent();
			setItemData(itemData);
			
			// Reset button handler
			resetBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					setItemData(getItemDataFromIntent());
				}
			});
			
			// Delete button handler
			deleteBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					deleteItem();
				}
			});

			// Long-clicked ImageView handler
			imageView.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					if (Environment.MEDIA_MOUNTED.equals(Environment
							.getExternalStorageState())) {
						launchEditIntentForEdit();
						return true;
					} else {
						Toast.makeText(context, 
								R.string.edit_item_message_no_external_storage, 
								Toast.LENGTH_SHORT)
								.show();
					}
					return false;
				}
			});
			
			// New Image handler
			newImageBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					newImageHandlerForEdit();		
				}
			});
			
			// Save Button handler
			saveBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					saveButtonHandlerForEdit();
				}
			});

			break;
		case ADD:
			//remove unneeded GUI components
			deleteBtn.setVisibility(View.GONE);
			isDirtyCheckbox.setVisibility(View.GONE);
			
			// Reset button handler
			resetBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					resetButtonHandlerForAdd();
				}
			});
			
			// New Image handler
			newImageBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					newImageHandlerForAdd();
				}
			});
			
			// Save Button handler
			saveBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					
					if (isInputValidForAdd()) {
						itemDatabaseHelper.saveItemDataRecord(
								createItemDataBuilderForAdd().buildByGender(up.getGender()));
						Toast.makeText(getApplicationContext(), "Item is saved", Toast.LENGTH_LONG).show();
						setResult(ADD_OK);
						finish();
					}			
				}
			});
			
			setItemData(null);
			break;
		}
	}

	/**
	 * This method only applies to Edit and View activity, NOT Add activity.
	 * @return
	 */
	private ItemData getItemDataFromIntent() {
		ItemData item = null;
		long id = getIntent().getLongExtra(Schema.Item.Cols.ID, 0);
		if (id == 0) {
			// Something must be wrong because Id of ItemData cannot be retrieved
			Log.i(TAG, "Cannot obtain Id");
			return null;
		}
		
		Cursor c = itemDatabaseHelper.queryItemFromId(id);
		if (c != null) {
			Log.i(TAG, "Cursor is NOT null");
			if (c.moveToFirst()) { // this is necessary to avoid CursorIndexOutOfBound 
				item = ItemDatabaseHelper.getItemDataFromCursor(c);
			}
		} else {
			Log.i(TAG, "Cursor is null");
		}
		
		return item;
	}

	private void initSpinner() {
		for (SpinnerValue value : SpinnerValue.values()) {
			Spinner field = (Spinner) findViewById(value.getId());
			SpinnerAdapter adapter = new ArrayAdapter<String>(
					getApplicationContext(),
					R.layout.simple_spinner_dropdown_item, 
					value.getValues());
			field.setAdapter(adapter);
			field.setSelection(adapter.getCount());
		}
	}
	
	private void initListerner() {
		final EditText itemName = (EditText) findViewById(R.id.item_name);
		final EditText itemDesc = (EditText) findViewById(R.id.item_description);
		final EditText itemTempMax = (EditText) findViewById(R.id.item_max_temp);
		final EditText itemTempMin = (EditText) findViewById(R.id.item_min_temp);
		final EditText itemAge = (EditText) findViewById(R.id.item_age);
		final EditText itemBrand = (EditText) findViewById(R.id.item_brand);
		final Spinner itemColor = (Spinner) findViewById(R.id.item_color);
		final Spinner itemCategory = (Spinner) findViewById(R.id.item_category);
		final Spinner itemStyle = (Spinner) findViewById(R.id.item_style);
		final Spinner itemMaterial = (Spinner) findViewById(R.id.item_material);
		
		itemCategory.setOnItemSelectedListener(new OnItemSelectedListener() {
			int oldPosition = -1;
			Map<ItemCategoryEnum, Integer> oldStyleSelection = new HashMap<ItemCategoryEnum, Integer>();
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == oldPosition ||
						position == itemCategory.getAdapter().getCount())
					return;
				
				ItemCategoryEnum currentCategory = ItemCategoryEnum.values()[position];
				
				switch (currentCategory) {
				case Top:
					if (oldPosition != -1)
						oldStyleSelection.put(ItemCategoryEnum.Bottom, itemStyle.getSelectedItemPosition());
					else
						oldStyleSelection.put(ItemCategoryEnum.Bottom,
								ItemStyleEnum.getAllBottomItemStyleEnumPerGender(
										gender).size() - 1);
					
					itemStyle.setAdapter(new ArrayAdapter<String>(
							getApplicationContext(),
							R.layout.simple_spinner_dropdown_item,
							ItemStyleEnum
									.getAllTopItemStyleEnumPerGender(gender)));
					break;
				case Bottom:
					if (oldPosition != -1)
						oldStyleSelection.put(ItemCategoryEnum.Top, itemStyle.getSelectedItemPosition());
					else
						oldStyleSelection.put(ItemCategoryEnum.Top,
								ItemStyleEnum.getAllTopItemStyleEnumPerGender(
										gender).size() - 1);
					
					itemStyle.setAdapter(new ArrayAdapter<String>(
							getApplicationContext(),
							R.layout.simple_spinner_dropdown_item,
							ItemStyleEnum
									.getAllBottomItemStyleEnumPerGender(gender)));
					break;
				}
				
				Integer oldSelection = oldStyleSelection.get(currentCategory);
				if (oldSelection != null)
					itemStyle.setSelection(oldSelection);
				else {
					if (itemData != null)
						itemStyle.setSelection(ItemStyleEnum
								.convertToIndexPerCateogryAndGender(
										itemData.getStyle(),
										itemData.getCategory(), gender));
					else
						itemStyle.setSelection(itemStyle.getAdapter().getCount());
				}
				
				oldPosition = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
	}

	/**
	 * @param itemData if null, reset all field to default values; otherwise, it fills new values 
	 */
	private void setItemData(final ItemData itemData) {
		final ImageView itemImage = (ImageView) findViewById(R.id.item_image);
		final EditText itemName = (EditText) findViewById(R.id.item_name);
		final EditText itemDesc = (EditText) findViewById(R.id.item_description);
		final EditText itemTempMax = (EditText) findViewById(R.id.item_max_temp);
		final EditText itemTempMin = (EditText) findViewById(R.id.item_min_temp);
		final EditText itemAge = (EditText) findViewById(R.id.item_age);
		final EditText itemBrand = (EditText) findViewById(R.id.item_brand);
		final Spinner itemColor = (Spinner) findViewById(R.id.item_color);
		final Spinner itemCategory = (Spinner) findViewById(R.id.item_category);
		final Spinner itemStyle = (Spinner) findViewById(R.id.item_style);
		final Spinner itemMaterial = (Spinner) findViewById(R.id.item_material);
		final CheckBox itemDirtyCheckbox = (CheckBox) findViewById(R.id.item_dirty);
		
		//reset all fields to default value
		if (itemData == null) {
			itemImage.setImageDrawable(null);
			EditText[] textFields = new EditText[]{
					itemName,
					itemDesc,
					itemTempMax,
					itemTempMin,
					itemAge,
					itemBrand
			};
			
			//set empty to all text fields
			for (EditText editText : textFields) {
				editText.setText("");
			}
			
			Spinner[] spinnerFields = new Spinner[] {
					itemColor,
					itemCategory,
					itemStyle,
					itemMaterial
			};
			
			//set isDirty to unchecked
			itemDirtyCheckbox.setChecked(false);
			
			//set default value to all spinner fields
			initSpinner();
			initListerner();
		}
		//set data from itemData to the view
		else {
			//set item image
			new ImageSubSampler(context).subSampleCroppedUri(itemData, itemImage, context);
			
			//set text field's value
			itemName.setText(itemData.getName());
			itemDesc.setText(itemData.getDescription());
			itemTempMax.setText(String.valueOf(itemData.getTempMax()));
			itemTempMin.setText(String.valueOf(itemData.getTempMin()));
			itemAge.setText(String.valueOf(itemData.getAge()));
			itemBrand.setText(itemData.getBrand());
			
			//set spinner field's value
			//Note: category must be set 1st because style depends on it 
			itemCategory.setSelection(itemData.getCategory().ordinal());
			int temp = ItemStyleEnum.convertToIndexPerCateogryAndGender(itemData.getStyle(), itemData.getCategory(), gender);
			Log.i(TAG, "id is - " + temp);
			
			switch (itemData.getCategory()) {
			case Top:
				itemStyle.setAdapter(new ArrayAdapter<String>(
						getApplicationContext(),
						R.layout.simple_spinner_dropdown_item,
						ItemStyleEnum
								.getAllTopItemStyleEnumPerGender(gender)));
				break;
			case Bottom:
				itemStyle.setAdapter(new ArrayAdapter<String>(
						getApplicationContext(),
						R.layout.simple_spinner_dropdown_item,
						ItemStyleEnum
								.getAllBottomItemStyleEnumPerGender(gender)));
				break;
			}

			itemStyle.setSelection(ItemStyleEnum.convertToIndexPerCateogryAndGender(itemData.getStyle(), itemData.getCategory(), gender));
			itemColor.setSelection(itemData.getColor().ordinal());
			itemMaterial.setSelection(itemData.getMaterial().ordinal());
			
			itemDirtyCheckbox.setChecked(itemData.getDirty());
		}
	}
	
	/**
	 * This method only applies to Edit activity.
	 * Assumption: currentItem is already populated from Intent when this 
	 * method is called.
	 */
	private void deleteItem() {
		String message = context.getString(R.string.view_item_dialog_deletion_message);

		//new AlertDialog.Builder(context) --> android alertdialog unable to add window token null is not for an application
		new AlertDialog.Builder(EditItemActivity.this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(R.string.view_item_dialog_deletion_title)
				.setMessage(message)
				.setPositiveButton(R.string.view_item_dialog_deletion_yes,
						new DialogInterface.OnClickListener() {
					
							@Override
							public void onClick(DialogInterface dialog, int which) {
								try {
									// delete original image and cropped image from SD card
									storage.deleteItemImagesFromStorage(itemData);
									
									// delete the entry in the item database
									itemDatabaseHelper.deleteItemDataRecord(itemData);
								} catch (Exception e) {
									Log.e(TAG, "Exception Caught => " + e.getMessage());
									e.printStackTrace();
									finish();
								}
								
								Toast.makeText(context, 
										"The item was deleted", 
										Toast.LENGTH_SHORT)
										.show();
								setResult(DELETE_OK);//returnCode 100 means delete ok
								finish();
							}
						})
				.setNegativeButton(R.string.view_item_dialog_deletion_no, null)
				.create()
				.show();
	}
	
	private void launchGalleryIntentForEdit() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, PICK_FROM_GALLERY_FOR_EDIT);
	}	

	private void launchCameraIntentForEdit() {
		Intent i1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		newImagePath = storage.getOutputImageFileUri(
				MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE, false);
		i1.putExtra(MediaStore.EXTRA_OUTPUT, newImagePath);
		startActivityForResult(i1, EditItemActivity.CAMERA_PIC_REQUEST_FOR_EDIT);
	}
	
	private void launchCropIntentForEdit(int type) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setType("image/*");

		List<ResolveInfo> list = getPackageManager().queryIntentActivities( intent, 0 );

		int size = list.size();

		if (size == 0) {	        
			Toast.makeText(this, "Can not find image crop app", Toast.LENGTH_SHORT).show();

			return;
		} else {
			final Spinner itemCategory = (Spinner) findViewById(R.id.item_category);
			if (PICK_FROM_GALLERY_FOR_EDIT == type) {
				newCropGalleryImagePath = storage.getOutputImageFileUri(
						MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE, true);
				intent.setData(newGalleryImagePath) // set the input to the picture in gallery
				.putExtra("outputX", ItemData.getCropWidthFromCategory(ItemCategoryEnum.valueOf(itemCategory.getSelectedItem().toString())))
				.putExtra("outputY", ItemData.getCropWidthFromCategory(ItemCategoryEnum.valueOf(itemCategory.getSelectedItem().toString())))
				.putExtra("aspectX", 1)
				.putExtra("aspectY", 1)
				.putExtra("scale", true)
				.putExtra("return-data", false) // don't return bitmap data
				.putExtra(MediaStore.EXTRA_OUTPUT, newCropGalleryImagePath); // set the name of the output cropped image
				
				// Create the following intent to avoid the following  error 
				// No Activity found to handle Intent { act=... (has extras) }
				Intent i = new Intent(intent);
				ResolveInfo res	= list.get(0); // ALDBG assume the first one is camera crop       	
				i.setComponent( new ComponentName(res.activityInfo.packageName, 
						res.activityInfo.name));        	
				startActivityForResult(i, CROP_FROM_GALLERY_FOR_EDIT);
			} else if (CAMERA_PIC_REQUEST_FOR_EDIT == type) {
				newCropImagePath = storage.getOutputImageFileUri(
						MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE, true);
				intent.setData(newImagePath) // set the input to the picture taken by camera
				.putExtra("outputX", ItemData.getCropWidthFromCategory(ItemCategoryEnum.valueOf(itemCategory.getSelectedItem().toString())))
				.putExtra("outputY", ItemData.getCropWidthFromCategory(ItemCategoryEnum.valueOf(itemCategory.getSelectedItem().toString())))
				.putExtra("aspectX", 1)
				.putExtra("aspectY", 1)
				.putExtra("scale", true)
				.putExtra("return-data", false) // don't return bitmap data
				.putExtra(MediaStore.EXTRA_OUTPUT, newCropImagePath); // set the name of the output cropped image
				
				// Create the following intent to avoid the following  error 
				// No Activity found to handle Intent { act=... (has extras) }
				Intent i = new Intent(intent);
				ResolveInfo res	= list.get(0); // ALDBG assume the first one is camera crop       	
				i.setComponent( new ComponentName(res.activityInfo.packageName, 
						res.activityInfo.name));        	
				startActivityForResult(i, CROP_FROM_CAMERA_FOR_EDIT);
			}

		}
	}

	private void launchEditIntentForEdit() {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setType("image/*");

		List<ResolveInfo> list = getPackageManager().queryIntentActivities( intent, 0 );

		int size = list.size();

		if (size == 0) {	        
			Toast.makeText(this, "Can not find image crop app", Toast.LENGTH_SHORT).show();

			return;
		} else {
			newEditImagePath = storage.getOutputImageFileUri(
					MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE, true);
			Uri oldUri = null;
			if (null == newImagePath) {
				oldUri = Uri.parse(itemData.getImageLink()); // user didn't take a new picture
			} else {
				oldUri = newImagePath; // user took a new picture, let's edit the new one
			}
			final Spinner itemCategory = (Spinner) findViewById(R.id.item_category);
			intent.setData(oldUri) // set the input to the picture taken by camera
			.putExtra("outputX", ItemData.getCropWidthFromCategory(ItemCategoryEnum.valueOf(itemCategory.getSelectedItem().toString()))) // equal to R.dimen.crop_bottom_width
			.putExtra("outputY", ItemData.getCropWidthFromCategory(ItemCategoryEnum.valueOf(itemCategory.getSelectedItem().toString()))) // equal to R.dimen.crop_bottom_height
			.putExtra("aspectX", 1)
			.putExtra("aspectY", 1)
			.putExtra("scale", true)
			.putExtra("return-data", false) // don't return bitmap data
			.putExtra(MediaStore.EXTRA_OUTPUT, newEditImagePath); // set the name of the output cropped image

			// Create the following intent to avoid the following  error 
			// No Activity found to handle Intent { act=... (has extras) }
			Intent i = new Intent(intent);
			ResolveInfo res	= list.get(0); // ALDBG assume the first one is camera crop       	
			i.setComponent( new ComponentName(res.activityInfo.packageName, 
					res.activityInfo.name));        	
			startActivityForResult(i, EDIT_FROM_FILE_FOR_EDIT);
		}
	}

	@Override
	public void onBackPressed() {

		if (actionType == ActionType.EDIT) {
			if (null != newImagePath) {
				// this mean a new image was taken, but user decides not to save
				storage.deleteFileIfExist(newImagePath);
			}
			newImagePath = null;

			if (null != newCropImagePath) {
				// this mean a new image was taken and cropped, 
				// but user decides not to save
				storage.deleteFileIfExist(newCropImagePath);
			}
			newCropImagePath = null;

			if (null != newEditImagePath) {
				storage.deleteFileIfExist(newEditImagePath);
			}
			newEditImagePath = null;

			if (null != newCropGalleryImagePath) {
				// this mean a new image was chosen from gallery, 
				// but user decides not to save. We need to delete the newly cropped
				// image but leave the image in gallery alone.
				storage.deleteFileIfExist(newCropGalleryImagePath);
			}
			newCropGalleryImagePath = null;
			
			setResult(DELETE_OK); // update image when get back to MyCloset
		}

		// Finish the Activity
		finish();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == EditItemActivity.CAMERA_PIC_REQUEST_FOR_EDIT) {
			if (resultCode == Activity.RESULT_OK) {
				// Image captured and saved to fileUri specified in the Intent
				if (null != newImagePath) {
					//image.setImageURI(newImagePath);
					launchCropIntentForEdit(CAMERA_PIC_REQUEST_FOR_EDIT);
				}
			} else {
				storage.deleteFileIfExist(newImagePath);
				newImagePath = null;
			}
		} else if (EditItemActivity.CROP_FROM_CAMERA_FOR_EDIT == requestCode) {
			if (resultCode == Activity.RESULT_OK) {
				if (null != newCropImagePath) {
					imageView.setImageURI(newCropImagePath);
					cropStatus = CropImageStatus.NEW_IMAGE_NEWER;
				}
			} else {
				storage.deleteFileIfExist(newCropImagePath);
				newCropImagePath = null;
			}
		} else if (EditItemActivity.EDIT_FROM_FILE_FOR_EDIT == requestCode) {
			if (resultCode == Activity.RESULT_OK) {
				if (null != newEditImagePath) {
					imageView.setImageURI(newEditImagePath);
					cropStatus = CropImageStatus.EDIT_IMAGE_NEWER;
				}
			} else {
				storage.deleteFileIfExist(newEditImagePath);
				newEditImagePath = null;
			}
		} else if (EditItemActivity.PICK_FROM_GALLERY_FOR_EDIT == requestCode) {
			if (resultCode == Activity.RESULT_OK) {
				newGalleryImagePath = data.getData();
				//image.setImageURI(newGalleryImagePath);
				launchCropIntentForEdit(PICK_FROM_GALLERY_FOR_EDIT);
			}
		} else if (EditItemActivity.CROP_FROM_GALLERY_FOR_EDIT == requestCode) {
			if (resultCode == Activity.RESULT_OK) {
				if (null != newCropGalleryImagePath) {
					imageView.setImageURI(newCropGalleryImagePath);
					cropStatus = CropImageStatus.NEW_IMAGE_NEWER;
				}
			} else {
				storage.deleteFileIfExist(newCropGalleryImagePath);
				newCropGalleryImagePath = null;
				newGalleryImagePath = null;
			}			
		} else if (CAMERA_PIC_REQUEST_FOR_ADD == requestCode) { // Add activity new image
			launchCropIntentForAdd();
		} else if (CROP_FROM_CAMERA_FOR_ADD == requestCode) {
			ItemData.ItemDataBuilder itemDataBuilder = createItemDataBuilderForAdd();
			new ImageSubSampler(context).subSampleCroppedUri(itemDataBuilder
					.buildByGender(up.getGender()), imageView, context);
		}
	}

	/**
	 * This is called by Add activity to launch camera
	 */
	private void launchCameraIntentForAdd() {		
		// Create a new intent to launch the MediaStore, Image capture function
		// Hint: use standard Intent from MediaStore class
		// See: http://developer.android.com/reference/android/provider/MediaStore.html
		Intent i1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		// Set the imagePath for this image file using the pre-made function
		// getOutputMediaFile to create a new filename for this specific image;
		// ALDBG we must use the whole getOutputMediaFileUri and Uri here, otherwise, image will not be displayed File newFile = getOutputMediaFile(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE);
		Uri newFileUri = storage.getOutputImageFileUri(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE, false);

		// Add the filename to the Intent as an extra. Use the Intent-extra name
		// from the MediaStore class, EXTRA_OUTPUT
		i1.putExtra(MediaStore.EXTRA_OUTPUT, newFileUri); // ALDBG this will not be used in CreateStoryFragment.java due to some bug - imagePath is never assigned a value
		//ALDBG we must use the whole Uri, otherwise, image will not be displayed  fragment.imagePath = Uri.parse(newFile.getPath());
		imagePathForAdd = newFileUri;
		
		// Start a new activity for result, using the new intent and the request
		// code CAMERA_PIC_REQUEST
		startActivityForResult(i1, CAMERA_PIC_REQUEST_FOR_ADD);
	}
	
	/**
	 * This is called by Add activity to create an ItemData from the views.
	 */
	private ItemData.ItemDataBuilder createItemDataBuilderForAdd() {
		final EditText itemName = (EditText) findViewById(R.id.item_name);
		final EditText itemDesc = (EditText) findViewById(R.id.item_description);
		final EditText itemTempMax = (EditText) findViewById(R.id.item_max_temp);
		final EditText itemTempMin = (EditText) findViewById(R.id.item_min_temp);
		final EditText itemAge = (EditText) findViewById(R.id.item_age);
		final EditText itemBrand = (EditText) findViewById(R.id.item_brand);
		final Spinner itemColor = (Spinner) findViewById(R.id.item_color);
		final Spinner itemCategory = (Spinner) findViewById(R.id.item_category);
		final Spinner itemStyle = (Spinner) findViewById(R.id.item_style);
		final Spinner itemMaterial = (Spinner) findViewById(R.id.item_material);
		
		ItemData.ItemDataBuilder itemDataBuilder = new ItemData.ItemDataBuilder(
				imagePathForAdd.toString(), 
				ItemColorEnum.valueOf(itemColor.getSelectedItem().toString()), 
				0,//Integer.valueOf(itemTempMin.getText().toString()), 
				0,//Integer.valueOf(itemTempMax.getText().toString()), 
				ItemCategoryEnum.valueOf(itemCategory.getSelectedItem().toString()),
				cropImagePathForAdd.toString())
				//.brand(itemBrand.getText().toString())
				//.age(Double.valueOf(itemAge.getText().toString()))
				.material(ItemMaterialEnum.valueOf(itemMaterial.getSelectedItem().toString()))
				.style(ItemStyleEnum.valueOf(itemStyle.getSelectedItem().toString()));

		if (!itemName.getText().toString().isEmpty()) {
			itemDataBuilder.name(itemName.getText().toString());
		}
		
		if (!itemDesc.getText().toString().isEmpty()) {
			itemDataBuilder.description(itemDesc.getText().toString());
		}
		
		if (!itemBrand.getText().toString().isEmpty()) {
			itemDataBuilder.brand(itemBrand.getText().toString());
		}
		
		if (!itemAge.getText().toString().isEmpty()) {
			itemDataBuilder.age(Double.valueOf(itemAge.getText().toString()));
		} else {
			itemDataBuilder.age(0);
		}

		return itemDataBuilder;
	}

	/**
	 * This is called by Add activity to launch crop action from image taken 
	 * from camera.
	 */
	private void launchCropIntentForAdd() {
    	Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");
        
        List<ResolveInfo> list = getPackageManager().queryIntentActivities( intent, 0 );
        
        int size = list.size();
        
        if (size == 0) {	        
        	Toast.makeText(this, "Can not find image crop app", Toast.LENGTH_SHORT).show();
        	
            return;
        } else {
    		// generate a new Uri for the new cropped image
        	cropImagePathForAdd = storage.getOutputImageFileUri(
    				MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE, true);
        	
        	final Spinner itemCategory = (Spinner) findViewById(R.id.item_category);
        	
        	intent.setData(imagePathForAdd) // set the input to the picture taken by camera
        	.putExtra("outputX", ItemData.getCropWidthFromCategory(ItemCategoryEnum.valueOf(itemCategory.getSelectedItem().toString())))
        	.putExtra("outputY", ItemData.getCropWidthFromCategory(ItemCategoryEnum.valueOf(itemCategory.getSelectedItem().toString())))
        	.putExtra("aspectX", 1)
        	.putExtra("aspectY", 1)
        	.putExtra("scale", true)
        	.putExtra("return-data", false) // don't return bitmap data
        	.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, cropImagePathForAdd); // set the name of the output cropped image
        	
        	// Create the following intent to avoid the following  error 
        	// No Activity found to handle Intent { act=... (has extras) }
    		Intent i = new Intent(intent);
        	ResolveInfo res	= list.get(0); // ALDBG assume the first one is camera crop       	
        	i.setComponent( new ComponentName(res.activityInfo.packageName, 
        			res.activityInfo.name));        	
        	startActivityForResult(i, CROP_FROM_CAMERA_FOR_ADD);
        }
	}

	/**
	 * This is called by Add activity to check if the views have valid values.
	 */
	private boolean isInputValidForAdd() {
		//final EditText itemTempMax = (EditText) findViewById(R.id.item_max_temp);
		//final EditText itemTempMin = (EditText) findViewById(R.id.item_min_temp);
		
		// image field cannot be null
		if (null == imagePathForAdd) {
			Toast.makeText(context, 
					R.string.add_item_message_no_image, 
					Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		
		/* tempMax >= tempMin
		if (Integer.valueOf(itemTempMax.getText().toString()) 
				< Integer.valueOf(itemTempMin.getText().toString())) {
			Toast.makeText(context, 
					R.string.add_item_message_tempMax_smaller_tempMin, 
					Toast.LENGTH_SHORT)
					.show();
			return false;					
		}
		*/
		
		return true;
	}
	
	private void resetButtonHandlerForAdd() {
		// Delete original image
		if (imagePathForAdd != null) {
			storage.deleteFileIfExist(imagePathForAdd);
		}
		
		// Delete cropped image and clear ImageView
		if (cropImagePathForAdd != null) {
			storage.deleteFileIfExist(cropImagePathForAdd);
			imageView.setImageResource(0);
		}
		
		// clear all of the views
		setItemData(null);
	}
	
	private void newImageHandlerForAdd() {
		// Category MUST be chosen
		final Spinner itemCategory = (Spinner) findViewById(R.id.item_category);
		String category = (String) itemCategory.getSelectedItem().toString();
		if ("".equals(category) || category.contains("-")) {
			Toast.makeText(context, 
					R.string.please_select_category, 
					Toast.LENGTH_LONG)
					.show();
			return;
		}

		// Style MUST be chosen
		final Spinner itemStyle = (Spinner) findViewById(R.id.item_style);
		String style = (String) itemStyle.getSelectedItem().toString();
		if ("".equals(style) || style.contains("-")) {
			Toast.makeText(context, 
					R.string.please_select_style, 
					Toast.LENGTH_LONG)
					.show();
			return;
		}

		// Color MUST be chosen
		final Spinner itemColor = (Spinner) findViewById(R.id.item_color);
		String color = (String) itemColor.getSelectedItem().toString();
		if ("".equals(style) || color.contains("-")) {
			Toast.makeText(context, 
					R.string.please_select_color, 
					Toast.LENGTH_LONG)
					.show();
			return;
		}

		// Material MUST be chosen
		final Spinner itemMaterial = (Spinner) findViewById(R.id.item_material);
		String material = (String) itemMaterial.getSelectedItem().toString();
		if ("".equals(style) || material.contains("-")) {
			Toast.makeText(context, 
					R.string.please_select_material, 
					Toast.LENGTH_LONG)
					.show();
			return;
		}

		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			launchCameraIntentForAdd();
		} else {
			Toast.makeText(context, 
					R.string.edit_item_message_no_external_storage, 
					Toast.LENGTH_SHORT)
					.show();	
		}
	}
	
	private void newImageHandlerForEdit() {
		// Create dialog for NewImage button
		final String [] items			= new String [] {"Take from camera", "Select from gallery"};				
		android.widget.ArrayAdapter<String> adapter	= new android.widget.ArrayAdapter<String> (
				getApplicationContext(), R.layout.simple_list_item_blacktext,items);
		AlertDialog.Builder builder		= new AlertDialog.Builder(this);
		builder.setTitle("Select Image");
		builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
			public void onClick( DialogInterface dialog, int item ) { //pick from camera
				if (item == 0) {
					if (Environment.MEDIA_MOUNTED.equals(Environment
							.getExternalStorageState())) {
						launchCameraIntentForEdit();
					} else {
						Toast.makeText(context, 
								R.string.edit_item_message_no_external_storage, 
								Toast.LENGTH_SHORT)
								.show();	
					}
				} else { // pick from gallery
					launchGalleryIntentForEdit();
				}
			}
		} );
		final AlertDialog dialog = builder.create();
		
		dialog.show();
	}
	
	private void saveButtonHandlerForEdit() {
		Toast.makeText(getApplicationContext(), "Save Button was clicked", Toast.LENGTH_LONG).show();
		
		if (!isInputValidForEdit()) {
			return;
		}

		// if Uri newImagePath is valid --> a new picture was taken
		// delete ItemData's original imageLink
		// update ItemData's original imageLink with the newly taken picture
		// invalidate variable Uri newImagePath 
		if (null != newImagePath) {
			storage.deleteFileIfExist(Uri.parse(itemData.getImageLink()));
			itemData.setImageLink(newImagePath.toString());
			newImagePath = null; // so that onBackPressed doesn't delete this
		}

		// if Uri newCropImagePath is valid --> a new picture was taken and cropped
		// delete ItemData's cropped cropImageLink
		// update ItemData's cropped cropImageLink with the newly cropped picture
		// invalidate variable Uri newCropImagePath 
		if (null != newCropImagePath) {
			if (CropImageStatus.NEW_IMAGE_NEWER == cropStatus) {
				storage.deleteFileIfExist(Uri.parse(itemData.getCropImageLink()));
				itemData.setCropImageLink(newCropImagePath.toString());
				newCropImagePath = null; // so that onBackPressed doesn't delete this						
			}
		}

		// if Uri newEditImagePath is valid --> the original ItemData was re-cropped
		// delete ItemData's cropped cropImageLink
		// update ItemData's cropped cropImageLink with the newly cropped picture
		// invalidate variable Uri newEditImagePath 
		if (null != newEditImagePath) {
			if (CropImageStatus.EDIT_IMAGE_NEWER == cropStatus) {
				storage.deleteFileIfExist(Uri.parse(itemData.getCropImageLink()));
				itemData.setCropImageLink(newEditImagePath.toString());
				newEditImagePath = null; // so that onBackPressed doesn't delete this												
			}
		}

		if (null != newCropGalleryImagePath) {
			if (CropImageStatus.NEW_IMAGE_NEWER == cropStatus) {
				if (null != newGalleryImagePath) {
					storage.deleteFileIfExist(Uri.parse(itemData.getImageLink()));
					itemData.setImageLink(newGalleryImagePath.toString());							
				}
				storage.deleteFileIfExist(Uri.parse(itemData.getCropImageLink()));
				itemData.setCropImageLink(newCropGalleryImagePath.toString());
				newCropGalleryImagePath = null; // so that onBackPressed doesn't delete this												
			}
		}

		// create new ItemData from views in Edit activity
		itemData = createItemDataBuilderForEdit().build();
		// Log.i(TAG, "buttonSave" + itemData.toString());

		// update the database with the newly create ItemData
		itemDatabaseHelper.updateItemDataRecord(itemData);

		onBackPressed(); // same as hit back key
	}
	
	/**
	 * This is called by Edit activity to check if the views have valid values.
	 */
	private boolean isInputValidForEdit() {
		// image field cannot be null
		if ((null == itemData.getImageLink()) // itemData doesn't have any image
				&& (null == newImagePath) // user didn't take new image
				&& (null == newEditImagePath) // user didn't edit the itemData image
				&& (null == newCropGalleryImagePath) // user didn't crop any image from gallery
				) {
			Toast.makeText(context, 
					R.string.add_item_message_no_image, 
					Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		
		return true;
	}

	/**
	 * This is called by Add activity to create an ItemData from the views.
	 */
	private ItemData.ItemDataBuilder createItemDataBuilderForEdit() {
		final EditText itemName = (EditText) findViewById(R.id.item_name);
		final EditText itemDesc = (EditText) findViewById(R.id.item_description);
		final EditText itemTempMax = (EditText) findViewById(R.id.item_max_temp);
		final EditText itemTempMin = (EditText) findViewById(R.id.item_min_temp);
		final EditText itemAge = (EditText) findViewById(R.id.item_age);
		final EditText itemBrand = (EditText) findViewById(R.id.item_brand);
		final Spinner itemColor = (Spinner) findViewById(R.id.item_color);
		final Spinner itemCategory = (Spinner) findViewById(R.id.item_category);
		final Spinner itemStyle = (Spinner) findViewById(R.id.item_style);
		final Spinner itemMaterial = (Spinner) findViewById(R.id.item_material);
		final CheckBox isDirtyCheckbox = (CheckBox) findViewById(R.id.item_dirty);
		
		ItemData.ItemDataBuilder itemDataBuilder = new ItemData.ItemDataBuilder(
				itemData.getImageLink(), 
				ItemColorEnum.valueOf(itemColor.getSelectedItem().toString()), 
				0,//Integer.valueOf(itemTempMin.getText().toString()), 
				0,//Integer.valueOf(itemTempMax.getText().toString()), 
				ItemCategoryEnum.valueOf(itemCategory.getSelectedItem().toString()),
				itemData.getCropImageLink())
				.id(itemData.getId())
				.material(ItemMaterialEnum.valueOf(itemMaterial.getSelectedItem().toString()))
				.style(ItemStyleEnum.valueOf(itemStyle.getSelectedItem().toString()))
				.dirty(isDirtyCheckbox.isChecked());

		if (!itemName.getText().toString().isEmpty()) {
			itemDataBuilder.name(itemName.getText().toString());
		}
		
		if (!itemDesc.getText().toString().isEmpty()) {
			itemDataBuilder.description(itemDesc.getText().toString());
		}
		
		if (!itemBrand.getText().toString().isEmpty()) {
			itemDataBuilder.brand(itemBrand.getText().toString());
		}
		
		if (!itemAge.getText().toString().isEmpty()) {
			itemDataBuilder.age(Double.valueOf(itemAge.getText().toString()));
		} else {
			itemDataBuilder.age(0);
		}

		return itemDataBuilder;
	}
		
}
